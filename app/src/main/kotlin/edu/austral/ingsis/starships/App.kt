package edu.austral.ingsis.starships

import game.Game
import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.ElementColliderType.*
import game.gameObject.GameObjectType
import game.gameObject.objects.GameObjectShape
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage

fun main() {
    launch(Starships::class.java)
}

class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()

    companion object {
        val STARSHIP_IMAGE_REF = ImageRef("starship", 70.0, 70.0)
        val game = Game()
    }

    override fun start(primaryStage: Stage) {
        game.start();
        val gameObjects = game.gameObjects
        var starship = ElementModel("starship", 300.0, 300.0, 40.0, 40.0, 180.0, Triangular, STARSHIP_IMAGE_REF)
        for (gameObject in gameObjects){

            val element = ElementModel(gameObject.id, gameObject.getxPosition(), gameObject.getyPosition(), gameObject.height, gameObject.width, gameObject.rotation, adaptShape(gameObject.shape), getImage(gameObject.type))
            facade.elements[gameObject.id] = element
            if (gameObject.type.equals(GameObjectType.STARSHIP)) starship = element
        }

        facade.timeListenable.addEventListener(TimeListener(facade.elements, game))
        facade.collisionsListenable.addEventListener(CollisionListener(game))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(starship, game))

        val scene = Scene(facade.view)
        keyTracker.scene = scene

        primaryStage.scene = scene
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    override fun stop() {
        facade.stop()
        keyTracker.stop()
    }
    fun adaptShape(shape : GameObjectShape) : ElementColliderType{
        return when(shape){
            GameObjectShape.RECTANGULAR -> Rectangular
            GameObjectShape.ELLIPTICAL -> Elliptical
            GameObjectShape.TRIANGULAR -> Triangular
        }
    }
    fun getImage(type : GameObjectType) : ImageRef? {
        return when(type){
            GameObjectType.STARSHIP -> STARSHIP_IMAGE_REF
            GameObjectType.BULLET -> null
            GameObjectType.METEOR -> null
        }
    }
}

class TimeListener(private val elements: Map<String, ElementModel>, private val game: Game) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        game.update()
        val gameObjects = game.gameObjects;
        for (gameObject in gameObjects){
            val element = elements.get(gameObject.id)
            val values = gameObject.values
            if (element != null) {
                element.x.set(values[0])
                element.y.set(values[1])
                element.rotationInDegrees.set(values[2])
            }
        }
    }
}

class CollisionListener(private val game: Game) : EventListener<Collision> {
    override fun handle(event: Collision) {
        println("${event.element1Id} ${event.element2Id}")
    }

}

class KeyPressedListener(private val starship: ElementModel, private val game: Game): EventListener<KeyPressed> {
    override fun handle(event: KeyPressed) {
        when(event.key) {
            KeyCode.W -> game.moveShip(0, 0.0, -5.0)
            KeyCode.S -> game.moveShip(0, 0.0, 5.0)
            KeyCode.A -> game.moveShip(0, -5.0, 0.0)
            KeyCode.D -> game.moveShip(0, 5.0, 0.0)
            KeyCode.LEFT -> game.rotateShip(0, -5.0)
            KeyCode.RIGHT -> game.rotateShip(0, 5.0)
            KeyCode.SPACE -> game.shoot(0)
            else -> {}
        }
    }



}