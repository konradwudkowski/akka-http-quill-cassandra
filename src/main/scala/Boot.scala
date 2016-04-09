import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import routes.RestApi
import service.ToDoServiceImpl
import utils.{ActorModuleImpl, ConfigurationModuleImpl}

object Main extends App {
  // configuring modules for application, cake pattern for DI
  val modules = new ConfigurationModuleImpl with ActorModuleImpl
  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()
  implicit val ec = modules.system.dispatcher

  //modules.suppliersDal.createTable()
  val service = ToDoServiceImpl(model)
  val bindingFuture = Http().bindAndHandle(service, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

}