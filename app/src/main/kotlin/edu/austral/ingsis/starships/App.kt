package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.ElementColliderType.*
import game.Game
import game.gameObject.objects.enums.Color
import game.gameObject.GameObject
import game.gameObject.objects.enums.GameObjectShape
import game.gameObject.objects.enums.GameObjectType
import game.gameObject.objects.Bullet
import game.gameObject.objects.enums.BulletType
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image

import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint

import javafx.stage.Stage
import javax.swing.text.html.ImageView
import kotlin.system.exitProcess


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
        val BULLET_PLASMA_BLUE = ImageRef("bullet_plasma_blue", 70.0, 70.0)
        val BULLET_PLASMA_RED = ImageRef("bullet_plasma_red", 70.0, 70.0)
        val BULLET_LIGHTNING_BLUE = ImageRef("bullet_lightning_blue", 70.0, 70.0)
        val BULLET_LIGHTNING_RED = ImageRef("bullet_lightning_red", 70.0, 70.0)
        val METEOR = ImageRef("meteor", 70.0, 70.0)
        val game = Game()
    }

    override fun start(primaryStage: Stage) {
        val pane = mainGameScene()
        val menu = menuScene(primaryStage, pane)

        facade.timeListenable.addEventListener(TimeListener(facade.elements, game, facade, this))
        facade.collisionsListenable.addEventListener(CollisionListener(game))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(game, this, primaryStage, pane, menu))

        keyTracker.scene = menu
        primaryStage.scene = menu
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }


    override fun stop() {
        facade.stop()
        keyTracker.stop()
        exitProcess(0)
    }

    fun adaptShape(shape : GameObjectShape) : ElementColliderType{
        return when(shape){
            GameObjectShape.RECTANGULAR -> Rectangular
            GameObjectShape.ELLIPTICAL -> Elliptical
            GameObjectShape.TRIANGULAR -> Triangular
        }
    }
    fun getImage(gameObject: GameObject) : ImageRef? {
        if (gameObject.type == GameObjectType.STARSHIP){
            if (gameObject.color == Color.RED) return STARSHIP_RED
            else return STARSHIP_BLUE
        }
        if (gameObject.type == GameObjectType.BULLET){
            var type = BulletType.LASER
            if (gameObject is Bullet){
                type = gameObject.bulletType;
            }
            if (gameObject.color == Color.RED){
                return if (type == BulletType.LIGHTNING) BULLET_LIGHTNING_RED
                else if (type == BulletType.PLASMA) BULLET_PLASMA_RED
                else BULLET_RED
            }
            else{
                return if (type == BulletType.LIGHTNING) BULLET_LIGHTNING_BLUE
                else if (type == BulletType.PLASMA) BULLET_PLASMA_BLUE
                else BULLET_BLUE
            }
        }
        else return METEOR
    }
    private fun mainGameScene(): StackPane {
        val pane = StackPane()
        val root = facade.view
        pane.children.addAll(root)
        root.id = "pane"
        return pane
    }
    private fun addGameObjects(){
        val gameObjects = game.gameObjects
        for (gameObject in gameObjects){
            facade.elements[gameObject.id] = ElementModel(gameObject.id, gameObject.getxPosition(), gameObject.getyPosition(), gameObject.height, gameObject.width, gameObject.rotation, adaptShape(gameObject.shape), getImage(gameObject))
        }
    }
    private fun menuScene(primaryStage: Stage, pane: StackPane): Scene {
        val title = Label("Starships")
        title.textFill = javafx.scene.paint.Color.PURPLE
        title.style = "-fx-font-family: VT323; -fx-font-size: 100;"

        val newGame = Label("New Game")
        newGame.textFill = javafx.scene.paint.Color.BLUE
        newGame.style = "-fx-font-family: VT323; -fx-font-size: 50"
        newGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            game.start(false)
            addGameObjects()
        }

        newGame.setOnMouseEntered {
            newGame.textFill = javafx.scene.paint.Color.MEDIUMPURPLE
            newGame.cursor = Cursor.HAND
        }

        newGame.setOnMouseExited {
            newGame.textFill = javafx.scene.paint.Color.BLUE
        }

        val loadGame = Label("Load Game")
        loadGame.textFill = javafx.scene.paint.Color.BLUE
        loadGame.style = "-fx-font-family: VT323; -fx-font-size: 50;"
        loadGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            game.start(true)
            addGameObjects()
        }
        loadGame.setOnMouseEntered {
            loadGame.textFill = javafx.scene.paint.Color.MEDIUMPURPLE
            loadGame.cursor = Cursor.HAND
        }

        loadGame.setOnMouseExited {
            loadGame.textFill = javafx.scene.paint.Color.BLUE
        }

        val newAndLoadGame = HBox(70.0)
        newAndLoadGame.alignment = Pos.CENTER
        newAndLoadGame.children.addAll(newGame, loadGame)

        val verticalLayout = VBox(50.0)
        verticalLayout.id = "menu"
        verticalLayout.alignment = Pos.CENTER
        verticalLayout.children.addAll(title, newAndLoadGame)

        val menu = Scene(verticalLayout)
        menu.stylesheets.add(this::class.java.classLoader.getResource("styles.css")?.toString())
        return menu
    }
    fun pauseScene(primaryStage: Stage, pane: StackPane, menu: Scene): Scene {
        val resume = Label("Resume")
        resume.textFill = javafx.scene.paint.Color.BLUE
        resume.style = "-fx-font-family: VT323; -fx-font-size: 50"
        resume.setOnMouseClicked {
            primaryStage.scene = menu
            primaryStage.scene.root = pane
            game.pauseOrResumeGame()
        }

        resume.setOnMouseEntered {
            resume.textFill = javafx.scene.paint.Color.MEDIUMPURPLE
            resume.cursor = Cursor.HAND
        }

        resume.setOnMouseExited {
            resume.textFill = javafx.scene.paint.Color.BLUE
        }
        var saved = false
        val saveGame = Label("Save game")
        saveGame.textFill = javafx.scene.paint.Color.BLUE
        saveGame.style = "-fx-font-family: VT323; -fx-font-size: 50;"
        saveGame.setOnMouseClicked {
            saveGame.textFill = javafx.scene.paint.Color.PURPLE
            game.saveGame()
            saved = true
        }
        saveGame.setOnMouseEntered {
            if (!saved){
                saveGame.textFill = javafx.scene.paint.Color.MEDIUMPURPLE
                saveGame.cursor = Cursor.HAND
            }
        }

        saveGame.setOnMouseExited {
            if (saved){
                saveGame.textFill = javafx.scene.paint.Color.PURPLE
            }
            else{
                saveGame.textFill = javafx.scene.paint.Color.BLUE
            }
        }

        val exitGame = Label("Exit game")
        exitGame.textFill = javafx.scene.paint.Color.BLUE
        exitGame.style = "-fx-font-family: VT323; -fx-font-size: 50;"
        exitGame.setOnMouseClicked {
            game.printLeaderBoard()
            stop()
        }
        exitGame.setOnMouseEntered {
            exitGame.textFill = javafx.scene.paint.Color.MEDIUMPURPLE
            exitGame.cursor = Cursor.HAND
        }

        exitGame.setOnMouseExited {
            exitGame.textFill = javafx.scene.paint.Color.BLUE
        }

        val verticalLayout = VBox(50.0)
        verticalLayout.id = "pause"
        verticalLayout.alignment = Pos.CENTER
        verticalLayout.children.addAll(
            resume,
            saveGame,
            exitGame
        )
        val pause = Scene(verticalLayout)
        pause.stylesheets.add(this::class.java.classLoader.getResource("styles.css")?.toString())
        return pause
    }
}

class TimeListener(
    private val elements: Map<String, ElementModel>,
    private val game: Game,
    private val facade: ElementsViewFacade,
    private val starships: Starships
) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        if(game.hasFinished()) {
            game.printLeaderBoard()
            starships.stop()
        }
        game.update()
        val gameObjects = game.gameObjects ?: return;
        for (gameObject in gameObjects){
            val element = elements[gameObject.id]
            if (element != null) {
                element.x.set(gameObject.getxPosition())
                element.y.set(gameObject.getyPosition())
                element.rotationInDegrees.set(gameObject.rotation)
                element.height.set(gameObject.height)
                element.width.set(gameObject.width)
            }
            else{
                facade.elements[gameObject.id] = ElementModel(gameObject.id, gameObject.getxPosition(), gameObject.getyPosition(), gameObject.height, gameObject.width, gameObject.rotation, starships.adaptShape(gameObject.shape), starships.getImage(gameObject))
            }
        }
        val eliminations = game.eliminated;

        for (eliminated in eliminations){
            if (elements.containsKey(eliminated)){
                facade.elements[eliminated] = null
            }
        }
    }
}

class CollisionListener(private val game: Game) : EventListener<Collision> {
    override fun handle(event: Collision) {
        game.handleCollision(event.element1Id, event.element2Id)
    }
}

class KeyPressedListener(
    private val game: Game,
    private val starships: Starships,
    private val primaryStage: Stage,
    private val pane: StackPane,
    private val menu: Scene
): EventListener<KeyPressed> {
    override fun handle(event: KeyPressed) {
        val map = game.keyBoardConfig;
        if (event.key == KeyCode.S && game.isPaused) game.saveGame()
        when(event.key) {
            map["accelerate-1"] -> game.moveShip("starship-1", true)
            map["stop-1"] -> game.moveShip("starship-1", false)
            map["rotate-left-1"] -> game.rotateShip("starship-1", -5.0)
            map["rotate-right-1"] -> game.rotateShip("starship-1", 5.0)
            map["shoot-1"] -> game.shoot("starship-1")
            map["accelerate-2"] -> game.moveShip("starship-2", true)
            map["stop-2"] -> game.moveShip("starship-2", false)
            map["rotate-left-2"] -> game.rotateShip("starship-2", -5.0)
            map["rotate-right-2"] -> game.rotateShip("starship-2", 5.0)
            map["shoot-2"] -> game.shoot("starship-2")
            KeyCode.P -> {
                game.pauseOrResumeGame()
                if (game.isPaused){
                    primaryStage.scene = starships.pauseScene(primaryStage, pane, menu)
                }
            }
            else -> {}
        }

    }
}
