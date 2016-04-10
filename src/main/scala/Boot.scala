import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import db.repositories.ToDoRepositoryImpl
import io.getquill._
import io.getquill.naming.Literal
import routes.RestApi


object Main extends App {
  // configuring modules for application, cake pattern for DI
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  //modules.suppliersDal.createTable()
  val db = source(new CassandraAsyncSourceConfig[Literal]("cassandra"))
  val service = new RestApi(new ToDoRepositoryImpl(db))
  val bindingFuture = Http().bindAndHandle(service.routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

}