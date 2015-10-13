/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.execution.crossdata

import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.types.{Metadata, DataType}
import org.apache.spark.sql.catalyst.plans.logical


case class NativeUDF(
                              name: String,
                              dataType: DataType,
                              children: Seq[Expression]
                              ) extends Expression with Unevaluable {

  override def toString: String = s"NativeUDF#$name(${children.mkString(",")})"
  override def nullable: Boolean = true
}

case class EvaluateNativeUDF(
                            udf: NativeUDF,
                            child: LogicalPlan,
                            resultAttribute: Attribute
                              ) extends logical.UnaryNode {

  def output: Seq[Attribute] = child.output :+ resultAttribute

  // References should not include the produced attribute.
  override def references: AttributeSet = udf.references

}

case class NativeUDFAttribute(name: String, dataType: DataType, nullable: Boolean = true)(
  val exprId: ExprId = NamedExpression.newExprId,
  val qualifiers: Seq[String] = Nil) extends Attribute with Unevaluable {

  def sameRef(other: NativeUDFAttribute): Boolean = this.exprId == other.exprId

  override def equals(other: Any): Boolean = other match {
    case ar: NativeUDFAttribute => name == ar.name && exprId == ar.exprId && dataType == ar.dataType
    case _ => false
  }

  override def semanticEquals(other: Expression): Boolean = other match {
    case ar: NativeUDFAttribute => sameRef(ar)
    case _ => false
  }

  override def hashCode: Int = {
    // See http://stackoverflow.com/questions/113511/hash-code-implementation
    var h = 17
    h = h * 37 + exprId.hashCode()
    h = h * 37 + dataType.hashCode()
    h = h * 37 + metadata.hashCode()
    h
  }

  override def newInstance(): NativeUDFAttribute =
    NativeUDFAttribute(name, dataType, nullable)(qualifiers = qualifiers)

  /**
   * Returns a copy of this [[AttributeReference]] with changed nullability.
   */
  override def withNullability(newNullability: Boolean): NativeUDFAttribute = {
    if (nullable == newNullability) {
      this
    } else {
      NativeUDFAttribute(name, dataType, newNullability)(exprId, qualifiers)
    }
  }

  override def withName(newName: String): NativeUDFAttribute = {
    if (name == newName) {
      this
    } else {
      NativeUDFAttribute(newName, dataType, nullable)(exprId, qualifiers)
    }
  }

  /**
   * Returns a copy of this [[AttributeReference]] with new qualifiers.
   */
  override def withQualifiers(newQualifiers: Seq[String]): NativeUDFAttribute = {
    if (newQualifiers.toSet == qualifiers.toSet) {
      this
    } else {
      NativeUDFAttribute(name, dataType, nullable)(exprId, newQualifiers)
    }
  }

  override def toString: String = s"$name#${exprId.id}$typeSuffix"
}

object EvaluateNativeUDF {
  def apply(udf: NativeUDF, child: LogicalPlan): EvaluateNativeUDF =
    new EvaluateNativeUDF(udf, child, NativeUDFAttribute(udf.name, udf.dataType, false)())
}

//case class NativeUDFEvaluation(udf: NativeUDF, output: Seq[Attribute], child: SparkPlan) extends SparkPlan

/*
*
* Analysis rule to replace resolved NativeUDFs by their evaluations as filters LogicalPlans
* These evaluations contain the information needed to refer the UDF in the native connector
* query generator.
*
*/
object ExtractNativeUDFs extends Rule[LogicalPlan] {
  override def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {
    case plan: EvaluateNativeUDF => plan
    case plan: LogicalPlan =>
      plan.expressions.
        flatMap(_.collect {case udf: NativeUDF => udf} ).
          find(_.resolved).
            map { case udf =>
              var evaluation: EvaluateNativeUDF = null

              val newChildren = plan.children flatMap { child =>
              // Check to make sure that the UDF can be evaluated with only the input of this child.
              // Other cases are disallowed as they are ambiguous or would require a cartesian
              // product.
              if (udf.references.subsetOf(child.outputSet)) {
                evaluation = EvaluateNativeUDF(udf, child)
                evaluation::Nil
              } else if (udf.references.intersect(child.outputSet).nonEmpty) {
                sys.error(s"Invalid NativeUDF $udf, requires attributes from more than one child.")
              } else {
                child::Nil
              }
            }

            assert(evaluation != null, "Unable to evaluate NativeUDF.  Missing input attributes.")

            logical.Project(
              plan.output, //plan.withNewChildren(newChildren)
              plan.transformExpressions {
                case u: NativeUDF if(u.fastEquals(udf)) => evaluation.resultAttribute
              }.withNewChildren(newChildren)
            )
          } getOrElse plan
  }
}