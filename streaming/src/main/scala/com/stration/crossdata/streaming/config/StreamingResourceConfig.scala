

package com.stration.crossdata.streaming.config

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stration.crossdata.streaming.constants.ApplicationConstants._

class StreamingResourceConfig extends TypesafeConfigComponent {

  override val config = new TypesafeConfig(None, None, Option(StreamingResourceConfig),
    Option(s"$ParentPrefixName" + s".$ConfigPrefixName"))
}