port module Main exposing (main)

import Browser
import Html as H exposing (Html)
import Html.Attributes as A exposing (class)
import Html.Events as E
import Json.Encode as Encode


main : Program Flags Model Msg
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type alias Flags =
    ()


init : Flags -> ( Model, Cmd Msg )
init _ =
    ( { todos = []
      , input = ""
      }
    , Cmd.none
    )


type alias Model =
    { todos : List Todo
    , input : String
    }


type alias Todo =
    { id : Int
    , title : String
    , status : TodoStatus
    }


type TodoStatus
    = Active
    | Complete


type Msg
    = NewTodo
    | Input String



-- update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NewTodo ->
            ( { model
                | input = ""
              }
            , createTodo model.input
            )

        Input s ->
            ( { model | input = s }, Cmd.none )



-- subscriptions


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none



-- ports


port createTodoPort : Encode.Value -> Cmd msg


createTodo : String -> Cmd msg
createTodo title =
    createTodoPort
        (Encode.object
            [ ( "title", Encode.string title )
            ]
        )


port setTodoStatusPort : Encode.Value -> Cmd msg


type alias SetTodoStatus =
    { id : Int
    , status : Bool
    }


setTodoStatus : Int -> Bool -> Cmd msg
setTodoStatus id status =
    setTodoStatusPort
        (Encode.object
            [ ( "id", Encode.int id )
            , ( "status", Encode.bool status )
            ]
        )



-- view


view : Model -> Html Msg
view model =
    H.div
        [ class "container"
        , class "mx-auto"
        , class "py-8"
        , class "flex"
        , class "flex-col"
        , class "items-center"
        ]
        [ H.h1
            [ class "text-center"
            , class "text-4xl"
            , class "mb-4"
            ]
            [ H.text "Scalajs Demo" ]
        , todoForm model
        , todoList model
        ]


todoForm : Model -> Html Msg
todoForm model =
    H.form
        [ class "flex"
        , class "flex-row"
        , class "justify-center"
        , class "w-4/12"
        , E.onSubmit NewTodo
        ]
        [ H.input
            [ A.type_ "input"
            , A.placeholder "What needs to be done?"
            , class "border-2 bg-gray-200 border-white hover:bg-white hover:border-gray-300 focus:outline-none focus:bg-white focus:shadow-outline focus:border-gray-300"
            , class "rounded-lg"
            , class "px-2 py-1"
            , class "w-full"
            , E.onInput Input
            , A.value model.input
            ]
            []
        ]


todoList : Model -> Html Msg
todoList model =
    H.div
        [ class "flex"
        , class "flex-row"
        , class "justify-center"
        , class "w-4/12"
        ]
        [ H.ul [] (model.todos |> List.map todoItem)
        ]


todoItem : Todo -> Html Msg
todoItem t =
    H.div [] [ H.text t.title ]
