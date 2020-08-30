package example

import cats._
import cats.implicits._
import endpoints4s.algebra
import endpoints4s.{Encoder, Decoder, Valid, Invalid}
import fs2.Chunk
import org.http4s._
import org.http4s.headers.`Content-Type`

trait JsonEntitiesFromEncodersAndDecoders
    extends algebra.JsonEntities
    with endpoints4s.http4s.server.EndpointsWithCustomErrors {

  type JsonRequest[A] = Decoder[String, A]
  type JsonResponse[A] = Encoder[A, String]

  def jsonRequest[A](implicit decoder: Decoder[String, A]): RequestEntity[A] = {
    req: org.http4s.Request[Effect] =>
      EntityDecoder
        .decodeBy(MediaType.application.json) { (msg: Media[Effect]) =>
          DecodeResult.success(EntityDecoder.decodeText(msg))
        }
        .decode(req, strict = true)
        .leftWiden[Throwable]
        .rethrowT
        .map { value =>
          decoder.decode(value) match {
            case Valid(a)     => Right(a)
            case inv: Invalid => Left(handleClientErrors(inv))
          }
        }
  }

  def jsonResponse[A](implicit encoder: Encoder[A, String]): ResponseEntity[A] =
    EntityEncoder[Effect, Chunk[Byte]]
      .contramap[A](value => Chunk.bytes(encoder.encode(value).getBytes))
      .withContentType(`Content-Type`(MediaType.application.json))
}
