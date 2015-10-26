====
FAQs
====

- **What's the point of Crossdata?**
    Crossdata is a product aimed to overcome the problems derived from the existence of different databases or data
    storage solutions within organizations.
    In order to maximize the value of big data applied to a given organization, all its data stores should be
    accessible by the big data solution.

    That can become quite a challenge when different departments took their own decisions on which technology to use
    for their datastores. Moreover, it might happen that external sources be consulted in order to add value to the
    internal data. Population census, location, historical data repositories, stock prices, ... are good examples
    of this.

    Crossdata enables you to seamlessly access different sources from Spark with optimization as a main
    non-functional requisite.

- **I thought Spark already took care of that, why should I use Crossdata?**
    Yes, Spark is already able to access any, supported by either its connectors or externally developed ones,
    datastore. The main value behind Crossdata is the O-word: **Optimization**.

    As many other solutions, Spark is able to use multiple datastores. However it generally won't make use of these
    sources optimized filtering, selection, joining or aggregation mechanisms. It will try to do its best for
    mainstream datastores (such as Cassandra) by **pushing down** some filters. However, more complex cases as joining
    within the same data source, aggregating, etc will be performed by Spark after
    having loaded, and unnecessary transmitted data between Spark workers, huge data sets as RDDs. Add that to the
    fact that Spark won't be able execute UDFs or built-in defined functions within concrete datastores.

    Crossdata tries to:
        - Execute operation as fully native queries to a datastore whenever is possible.
        - Otherwise, push-down as many parts of the operation as possible. That is, generate optimized native queries to fulfill as many parts of the operation as possible.


- **Which datastores can Crossdata make use of?**
    Crossdata is a library expanding Spark functionality. Therefore, any data source supported by Spark is also
    supported by Crossdata. Nevertheless, the optimizations set and advantages provided by Crossdata are to be noted
    when accessing to *MongoDB* and *Cassandra* datastores.

- **How bad is the resource overload when using Crossdata compared to access to DBs through Spark data sources?**
    There is not overload. On the contrary, Crossdata just adds optimizations whenever possible falling back to
    default Spark behaviour otherwise.

- **I'm already used to SparkSQL syntax, how long will it take to learn Crossdata SQL?**
    As mentioned above, Crossdata is just an expansion for Spark. If you already know how to use SparkSQL then
    you are able to start using Crossdata. To fully make use of Crossdata advantages you may want to check the
    details of the expansion grammar `at <GrammarExp.rst>`__

- **How can I integrate Crossdata with my Spark application?**
    By just adding Crossdata dependency to your project and replacing the use of *SQLContext* by *XDContext*

- **I'd like to use a non-supported-by-crossdata datasource, have I to drop Crossdata from my project?**
    Not at all! The fallback behaviour will be covered by Spark functionality.

- **I keep getting "LogicalPlan ... cannot be executed natively" messages but still retrieving right results. What's going on?**
    That is exactly what happens when an operation can be performed by just executing a native query. It doesn't
    necessarily mean that all the operation will be executed as a classic *SparkSQL* operation. In many cases, some
    parts will be pushed-down to native queries being the remaining sub-operations executed by Spark.




