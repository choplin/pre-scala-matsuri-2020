package example

import cats._
import cats.implicits._
import cats.effect._
import cats.effect.concurrent.Ref
import endpoints4s.algebra
import endpoints4s.openapi.model.OpenApi
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import example.endpoint.{Todo, CreateTodo, TodoEndpoints}

class CounterRoutes[F[_]: Sync: Monad](ref: Ref[F, Map[Int, Todo]])
    extends endpoints4s.http4s.server.Endpoints[F]
    with endpoints4s.http4s.server.JsonEntitiesFromSchemas
    with TodoEndpoints {
  val routes: HttpRoutes[F] = HttpRoutes.of(
    routesFromEndpoints(
      createTodo.implementedByEffect(req =>
        for {
          todos <- ref.get
          newId = todos.size
          todo = Todo(newId, req.title, true)
          _ <- ref.set(todos.updated(newId, todo))
        } yield todo
      ),
      setTodoStatus.implementedByEffect(req =>
        for {
          _ <- ref.update { todos =>
            todos
              .get(req.id)
              .fold(todos)(t =>
                todos.updated(t.id, t.copy(status = req.status))
              )
          }
        } yield ()
      )
    )
  )
}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      ref <- Ref.of[IO, Map[Int, Todo]](Map.empty)
      counterRoutes = new CounterRoutes[IO](ref)
      exit <-
        BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(counterRoutes.routes.orNotFound)
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
    } yield exit
}
