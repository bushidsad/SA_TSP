package noah;


    import java.util.Random;

    public class TravelingSalesmanProblem {

        private static final int INF = Integer.MAX_VALUE; // 无穷大

        private static final double INITIAL_TEMPERATURE = 1000; // 初始温度
        private static final double FINAL_TEMPERATURE = 0.1; // 终止温度
        private static final double COOLING_RATE = 0.99; // 降温速度

        private static final int MAX_ITERATIONS = 2000; // 最大迭代次数

        private int[][] adjacencyMatrix; // 邻接矩阵
        private int cityCount; // 城市数量

        public TravelingSalesmanProblem(int[][] adjacencyMatrix) {
            this.adjacencyMatrix = adjacencyMatrix;
            this.cityCount = adjacencyMatrix.length;
        }

        public int[] solve() {
            int[] currentSolution = generateRandomSolution(); // 生成初始解
            int[] bestSolution = currentSolution.clone(); // 记录最优解

            double temperature = INITIAL_TEMPERATURE;
            int iteration = 1;

            while (temperature > FINAL_TEMPERATURE && iteration <= MAX_ITERATIONS) {
                int[] newSolution = getNextSolution(currentSolution); // 获取新解
                int currentDistance = calculateRouteDistance(currentSolution); // 当前解的距离
                int newDistance = calculateRouteDistance(newSolution); // 新解的距离

                if (acceptNewSolution(currentDistance, newDistance, temperature)) {
                    currentSolution = newSolution;
                    if (newDistance < calculateRouteDistance(bestSolution)) {
                        bestSolution = newSolution.clone();
                    }
                }

                temperature *= COOLING_RATE;
                iteration++;
            }

            return bestSolution;
        }

        // 生成随机解
        private int[] generateRandomSolution() {
            int[] solution = new int[cityCount];
            for (int i = 0; i < cityCount; i++) {
                solution[i] = i;
            }
            shuffleArray(solution);
            return solution;
        }

        // 获取下一个解
        private int[] getNextSolution(int[] solution) {
            int[] newSolution = solution.clone();
            Random random = new Random();
            int i = random.nextInt(cityCount);
            int j = random.nextInt(cityCount);
            int temp = newSolution[i];
            newSolution[i] = newSolution[j];
            newSolution[j] = temp;
            return newSolution;
        }

        // 计算路径距离
        private int calculateRouteDistance(int[] solution) {
            int distance = 0;
            for (int i = 0; i < cityCount - 1; i++) {
                int from = solution[i];
                int to = solution[i + 1];
                if (adjacencyMatrix[from][to] == INF) {
                    return INF;
                }
                distance += adjacencyMatrix[from][to];
            }
            int lastCity = solution[cityCount - 1];
            int firstCity = solution[0];
            if (adjacencyMatrix[lastCity][firstCity] == INF) {
                return INF;
            }
            distance += adjacencyMatrix[lastCity][firstCity];
            return distance;
        }

        // 判断是否接受新解
        private boolean acceptNewSolution(int currentDistance, int newDistance, double temperature) {
            if (newDistance < currentDistance) {
                return true;
            }
            double acceptanceProbability = Math.exp((currentDistance - newDistance) / temperature);
            return Math.random() < acceptanceProbability;
        }

        // 打乱数组
        private void shuffleArray(int[] array) {
            Random random = new Random();
            for (int i = array.length - 1; i > 0; i--) {
                int index = random.nextInt(i + 1);
                int temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }

        public static void main(String[] args) {
            int[][] adjacencyMatrix = new int[][] {
                    {0, 6, 2, 1, INF, INF},
                    {6, 0, 6, INF, 3, 10},
                    {2, 6, 0, 2, 2, 4},
                    {1, INF, 2, 0, INF, 5},
                    {INF, 3, 2, INF, 0, 3},
                    {INF, INF, 4, 5, 3, 0}
            };
            TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(adjacencyMatrix);
            int[] solution = tsp.solve();
            int distance = tsp.calculateRouteDistance(solution);
            System.out.println("最短路径长度为：" + distance);
            System.out.print("最短路径为：");
            for (int i = 0; i < solution.length; i++) {
                System.out.print(solution[i] + " ");
            }
        }

}
