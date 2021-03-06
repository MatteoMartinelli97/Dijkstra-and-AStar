import java.awt.*
import javax.swing.JFrame
import javax.swing.JPanel

class GridFrame(g :Grid2D, path: ArrayDeque<Int>, nodes: MutableList<Int>? = null, color: Color = Color.CYAN, title : String) : JFrame(title) {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        val grid = DrawGrid(g, color)
        contentPane.add(grid)
        grid.addPath(path)
        if (nodes != null) grid.addVisitedNodes(nodes)
        val source = g.getCoordinates(path.first())
        val target = g.getCoordinates(path.last())
        grid.setSource(source.first, source.second)
        grid.setTarget(target.first, target.second)
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    fun addNodes(nodes : MutableList<Int>) {

    }
}


class DrawGrid(val grid : Grid2D,
               val color: Color = Color.CYAN
) : JPanel() {

    private val squares: MutableList<Rectangle> = mutableListOf()
    private val blocks : MutableList<Rectangle> = mutableListOf()
    private val bestPath : MutableList<Rectangle> = mutableListOf()
    private val visitedNodes : MutableList<Rectangle> = mutableListOf()
    private var source  = Rectangle()
    private var target = Rectangle()
    private val step = PREF_W/grid.nSquares
    private val sqSize = step - 2

    init {
        for (b in grid.blocks) {
            val rect = Rectangle(b.second * step , b.first * step, sqSize, sqSize)
            blocks.add(rect)
        }

        for (v in grid.freeV) {
            val rect = Rectangle(v.second * step , v.first * step, sqSize, sqSize)
            squares.add(rect)
        }
    }

    fun getSqSize () : Int {return sqSize}

    fun addSquare(row: Int, col: Int) {
        val rect = Rectangle(col * step, row * step, sqSize, sqSize)
        squares.add(rect)
    }

    fun addBlock(row: Int, col: Int) {
        val rect = Rectangle(col * step, row * step, sqSize, sqSize)
        blocks.add(rect)
    }
    fun addPath(path : ArrayDeque<Int>) {
        for (p in path) {
            val coor = grid.getCoordinates(p)
            val rect = Rectangle(coor.second * step, coor.first * step, sqSize, sqSize)
            bestPath.add(rect)
        }
    }

    fun addVisitedNodes(nodes: MutableList<Int>) {
        for (n in nodes){
            val coor = grid.getCoordinates(n)
            val rect = Rectangle(coor.second * step, coor.first * step, sqSize, sqSize)
            visitedNodes.add(rect)
        }
    }
    fun setSource (row: Int, col: Int) {
        source = Rectangle(col * step, row * step, sqSize, sqSize)
    }

    fun setTarget (row: Int, col: Int) {
        target = Rectangle(col * step, row * step, sqSize, sqSize)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(PREF_W, PREF_H)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D

        for (rect in squares) {
            g2.color = Color.BLACK
            g2.draw(rect)
        }
        for (n in visitedNodes) {
            g2.color = Color.BLACK
            g2.draw(n)
            g2.color =  Color(239, 229, 12)
            g2.fill(n)
        }

        for (step in bestPath) {
            g2.color = Color.BLACK
            g2.draw(step)
            g2.color = color
            g2.fill(step)
        }
        for (b in blocks) {
            g2.color = Color.BLACK
            g2.draw(b)
            g2.color =  Color(97, 106, 107)
            g2.fill(b)
        }


        g2.color = Color.BLACK
        g2.draw(source)
        g2.draw(target)

        g2.color = Color.RED
        g2.fill(source)

        g2.color = Color.GREEN
        g2.fill(target)
    }

    companion object {
        private const val PREF_W = 750
        private const val PREF_H = PREF_W
    }
}