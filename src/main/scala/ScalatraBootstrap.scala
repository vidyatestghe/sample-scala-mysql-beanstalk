import org.slf4j.LoggerFactory
import org.scalatra._
import javax.servlet.ServletContext
import com.shippable._

/**
 * This is the ScalatraBootstrap bootstrap file. You can use it to mount servlets or
 * filters. It's also a good place to put initialization code which needs to
 * run at application start (e.g. database configurations), and init params.
 */
class ScalatraBootstrap extends LifeCycle {

  val logger = LoggerFactory.getLogger(getClass)

  override def init(context: ServletContext) {
    val db = DbConnection.connect
    context.mount(SlickApp(db), "/*")
  }
}
