import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import utils.{ActorModuleImpl, ConfigurationModuleImpl}
import db.repositories.ToDoRepositoryImpl
import io.getquill._
import io.getquill.naming.Literal
import io.getquill.sources.cassandra.CassandraAsyncSource
import routes.RestApi


object Main extends App {
  // configuring modules for application, cake pattern for DI
  val modules = new ConfigurationModuleImpl with ActorModuleImpl
  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()
  implicit val ec = modules.system.dispatcher

  //modules.suppliersDal.createTable()
  val db = source(new CassandraAsyncSourceConfig[Literal]("cassandra"))
  val service = new RestApi(new ToDoRepositoryImpl(db))
  val bindingFuture = Http().bindAndHandle(service.routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

}