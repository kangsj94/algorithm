package programmers.kakaoInternship;

import java.util.*;

public class EscapeMaze {
    public int solution(int n, int start, int end, int[][] roads, int[] traps) {
        List<Edge>[] adj = new ArrayList[n + 1];
        Map<Integer, Integer> trap = new HashMap<>();
        int ans = (int) 1e9;

        for (int i = 1; i <= n; i++)
            adj[i] = new ArrayList<>();

        for (int[] road : roads) {
            adj[road[0]].add(new Edge(road[1], road[2], 0));
            adj[road[1]].add(new Edge(road[0], road[2], 1));
        }

        for (int i = 0; i < traps.length; i++)
            trap.put(traps[i], i);

        // 다익스트라
        int[][] dist = new int[n + 1][1 << 10];
        for (int i = 1; i <= n; i++)
            Arrays.fill(dist[i], (int) 1e9);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        pq.add(new Edge(start, 0, 0));
        dist[start][0] = 0;

        while (!pq.isEmpty()) {
            Edge cur = pq.poll();
            int curNode = cur.to;
            int curCost = cur.cost;
            int curState = cur.state;

            // end에 도착했을 때
            if (curNode == end) {
                ans = Math.min(ans, curCost);
                continue;
            }

            if (curCost > dist[curNode][curState])
                continue;

            for (Edge next : adj[curNode]) {
                int nextNode = next.to;
                int nextCost = next.cost;
                int isReverse = next.state;

                if (isReverse != (isConnected(curNode, nextNode, curState, trap) ? 1 : 0))
                    continue;

                int nextState = getNextState(curState, nextNode, trap);
                nextCost += curCost;

                if (nextCost >= dist[nextNode][nextState])
                    continue;

                dist[nextNode][nextState] = nextCost;
                pq.add(new Edge(nextNode, nextCost, nextState));
            }
        }

        return ans;
    }

    public int getNextState(int curState, int nextNode, Map<Integer, Integer> trap) {
        if (trap.containsKey(nextNode))
            curState ^= (1 << trap.get(nextNode));
        return curState;
    }


    public boolean isConnected(int curNode, int nextNode, int curState, Map<Integer, Integer> trap) {
        boolean currNodeTrapChk = false, nextNodeTrapChk = false;

        if (trap.containsKey(curNode))
            currNodeTrapChk = ((curState & (1 << trap.get(curNode))) != 0);

        if (trap.containsKey(nextNode))
            nextNodeTrapChk = ((curState & (1 << trap.get(nextNode))) != 0);

        return currNodeTrapChk ^ nextNodeTrapChk;
    }

    class Edge implements Comparable<Edge> {
        int to, cost, state;

        Edge(int to, int cost, int state) {
            this.to = to;
            this.cost = cost;
            this.state = state;
        }

        @Override
        public int compareTo(Edge o) {
            return this.cost - o.cost;
        }
    }

}
