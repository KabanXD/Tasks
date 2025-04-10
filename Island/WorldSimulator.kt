package island

class WorldSimulator {
    private val island = Island(10, 10)

    fun startSimulation() {
        println("Starting Island Simulation...")
        island.populate()
        var day = 1
        while (true) {
            println("\n--- Day $day ---")
            island.simulateDay()
            island.printStats()
            Thread.sleep(1000) // Задержка 1 секунда
            day++
        }
    }
}