package example.endpoint

import endpoints4s.{algebra, generic}
import scala.scalajs.js.annotation._

case class Todo(id: Int, title: String, status: Boolean)

case class CreateTodo(title: String)
case class SetTodoStatus(id: Int, status: Boolean)

trait TodoEndpoints
    extends algebra.Endpoints
    with algebra.JsonEntitiesFromSchemas
    with generic.JsonSchemas {

  val createTodo: Endpoint[CreateTodo, Todo] =
    endpoint(
      post(path / "todo" / "create", jsonRequest[CreateTodo]),
      ok(jsonResponse[Todo])
    )

  val setTodoStatus: Endpoint[SetTodoStatus, Unit] =
    endpoint(
      post(path / "todo" / "status", jsonRequest[SetTodoStatus]),
      ok(emptyResponse)
    )

  implicit lazy val createTodoSchema: JsonSchema[CreateTodo] = genericJsonSchema
  implicit lazy val todoSchema: JsonSchema[Todo] = genericJsonSchema
  implicit lazy val setTodoStatusSchema: JsonSchema[SetTodoStatus] =
    genericJsonSchema
}
