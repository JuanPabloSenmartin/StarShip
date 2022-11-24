package edu.austral.ingsis.starships

import game.Game
import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.ElementColliderType.*
import game.gameObject.GameObject
import game.gameObject.GameObjectType
import game.gameObject.Color
import game.gameObject.GameObjectShape
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
        val STARSHIP_RED = ImageRef("starship_red", 70.0, 70.0)
        val STARSHIP_BLUE = ImageRef("starship_blue", 70.0, 70.0)
        val BULLET_RED = ImageRef("bullet_red", 70.0, 70.0)
        val BULLET_BLUE = ImageRef("bullet_blue", 70.0, 70.0)
        val METEOR = ImageRef("meteor", 70.0, 70.0)
        val game = Game()
    }

    override fun start(primaryStage: Stage) {
        game.start(false);
        val gameObjects = game.gameObjects
        for (gameObject in gameObjects){
            facade.elements[gameObject.id] = ElementModel(gameObject.id, gameObject.getxPosition(), gameObject.getyPosition(), gameObject.height, gameObject.width, gameObject.rotation, adaptShape(gameObject.shape), getImage(gameObject))
        }

        facade.timeListenable.addEventListener(TimeListener(facade.elements, game))
        facade.collisionsListenable.addEventListener(CollisionListener(game))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(game))

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
    fun getImage(gameObject: GameObject) : ImageRef? {
        return if (gameObject.type == GameObjectType.STARSHIP && gameObject.color == Color.RED) STARSHIP_RED
        else if(gameObject.type == GameObjectType.STARSHIP && gameObject.color == Color.BLUE) STARSHIP_BLUE
        else if (gameObject.type == GameObjectType.BULLET && gameObject.color == Color.RED) BULLET_RED
        else if (gameObject.type == GameObjectType.BULLET && gameObject.color == Color.BLUE) BULLET_BLUE
        else METEOR
    }
}

class TimeListener(private val elements: Map<String, ElementModel>, private val game: Game) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        if(game.hasFinished()) {
            game.printLeaderBoard()
            game.resetGame()
        }
        game.update()
        val gameObjects = game.gameObjects;
        for (gameObject in gameObjects){
            val element = elements.get(gameObject.id)
            val values = gameObject.values
            if (element != null) {
                element.x.set(values[0])
                element.y.set(values[1])
                element.rotationInDegrees.set(values[2])
                element.height.set(values[3])
                element.width.set(values[4])
            }
        }
    }
}

class CollisionListener(private val game: Game) : EventListener<Collision> {
    override fun handle(event: Collision) {
//        println("${event.element1Id} ${event.element2Id}")
        game.handleCollision(event.element1Id, event.element2Id)
    }

}

class KeyPressedListener(private val game: Game): EventListener<KeyPressed> {
    override fun handle(event: KeyPressed) {
        val map = game.keyBoardConfig;
        if (event.key == KeyCode.S && game.isPaused) game.saveGame()
        when(event.key) {
            map["accelerate-1"] -> game.moveShip(0, true)
            map["stop-1"] -> game.moveShip(0, false)
            map["rotate-left-1"] -> game.rotateShip(0, -5.0)
            map["rotate-right-1"] -> game.rotateShip(0, 5.0)
            map["shoot-1"] -> game.shoot(0)
            KeyCode.P -> game.pauseOrResumeGame()
            else -> {}
        }
        if (game.players.size == 2){
            when(event.key) {
                map["accelerate-2"] -> game.moveShip(1, true)
                map["stop-2"] -> game.moveShip(1, false)
                map["rotate-left-2"] -> game.rotateShip(1, -5.0)
                map["rotate-right-2"] -> game.rotateShip(1, 5.0)
                map["shoot-2"] -> game.shoot(1)
                else -> {}
            }
        }
    }



}