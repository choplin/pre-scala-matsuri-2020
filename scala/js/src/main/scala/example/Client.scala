package example

import endpoints4s.xhr
import example.endpoint.{Todo, CreateTodo, SetTodoStatus, TodoEndpoints}

import scala.scalajs.js
import scala.scalajs.js.annotation._

object TodoClientImpl
    extends TodoEndpoints
    with xhr.thenable.Endpoints
    with xhr.JsonEntitiesFromSchemas

@JSExportTopLevel("CounterClient")
object ounterClient {
  @JSExport
  def createTodo(req: CreateTodo): js.Thenable[Todo] =
    TodoClientImpl.createTodo(req)
  @JSExport
  def increment(req: SetTodoStatus): js.Thenable[Unit] =
    TodoClientImpl.setTodoStatus(req)
}
