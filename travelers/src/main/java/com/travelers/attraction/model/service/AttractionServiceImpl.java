package com.travelers.attraction.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelers.attraction.model.mapper.AttractionMapper;
import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.attraction.model.dto.GugunDto;
import com.travelers.attraction.model.dto.SidoDto;

@Service
public class AttractionServiceImpl implements AttractionService {
	public static final int INF = 1000000000, INVALID = 100000000;
	// 모든 도시 방문한 경우
	public static int FULL;
	// 시작점 0번 도시로 고정
	public static final int START = 0;

	private AttractionMapper attractionMapper;
	
	@Autowired
	public AttractionServiceImpl(AttractionMapper attractionMapper) {
		super();
		this.attractionMapper = attractionMapper;
	}
	
	@Override
	public List<SidoDto> getSido() throws Exception {
		return attractionMapper.getSido();
	}

	@Override
	public List<GugunDto> getGugun(int sidoCode) throws Exception {
		// TODO Auto-generated method stub
		return attractionMapper.getGugun(sidoCode);
	}
	
	@Override
	public List<AttractionDto> getAttractionList(int sidoCode, int gugunCode, String query, int contentType) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("%").append(query).append("%");
		map.put("sidoCode", sidoCode);
		map.put("gugunCode", gugunCode);
		map.put("query", sb.toString());
		map.put("contentType", contentType);
		return attractionMapper.getAttractionList(map);
	}

	@Override
	public AttractionDto getAttraction(int contentId) throws Exception {
		return attractionMapper.getAttraction(contentId);
	}

	@Override
	public List<AttractionDto> getScheduleInfo(List<Integer> scheduleList) throws Exception {
		// 비어 있는 경우
		if(scheduleList.isEmpty()) return new ArrayList<AttractionDto>();
		
		List<AttractionDto> attractionList = attractionMapper.getScheduleInfo(scheduleList);
		// 하나만 있는 경우 그대로 리턴
		if(attractionList.size() == 1) return attractionList;
		
		// 도시의 수
		final int N = attractionList.size();
		// 도시 좌표 입력
		Coord[] cities = new Coord[N];
		int x, y;
		for(int i = 0; i < N; i++) {
			cities[i] = new Coord(attractionList.get(i).getLatitude(), attractionList.get(i).getLongitude());
		}
		
		// 모든 도시 방문한 경우
		FULL = (1 << N) - 1;
		// 다른 도시 방문하는 비용 인접 행렬
		double[][] cost = getCost(N, cities);
		// 현재 curr의 위치에 있고, bitmask 상태에서 다른 모든 점을 방문하는 최단 거리 = tsp[curr][bitmask]
		double[][] tsp = new double[N][FULL + 1];
		// 시작 위치 초기화
		for(int i = 0; i < N; i++) {
			Arrays.fill(tsp[i], INF);
		}
		
		List<AttractionDto> tspRoute = new ArrayList<>();
		System.out.println("최단 거리: " + dfs(START, 1 << START, N, cost, tsp));
		// TSP 경로 저장
		printRoute(START, 1 << START, N, cost, tsp, tspRoute, attractionList);
		
		return tspRoute;
	}

	public static void printRoute(int curr, int bitmask, int N, double[][] cost, double[][] tsp, List<AttractionDto> tspRoute, List<AttractionDto> attractionList) {
		// 현재 위치 경로에 추가
		tspRoute.add(attractionList.get(curr));
		
		// 모든 경로를 방문했다면 중단
		if(bitmask == FULL) return;
		
		double minCost = INF; 
		int minIdx = -1;
		int next;
		for(int i = 0; i < N; i++) {
			next = bitmask | (1 << i);
			// 이미 방문한 경우 생략
			if(next == bitmask) continue;
			
			if(cost[curr][i] + tsp[i][next] < minCost) {
				minCost = cost[curr][i] + tsp[i][next];
				minIdx = i;
			}
		}
		
		// 다음 도시 탐색
		printRoute(minIdx, bitmask | (1 << minIdx), N, cost, tsp, tspRoute, attractionList);
	}
	
	// curr에서 시작했을 때, 다음 도시에서 남은 점을 방문하는 최소 거리 탐색
	public static double dfs(int curr, int bitmask, int N, double[][] cost, double[][] tsp) {
		if(bitmask == FULL) {
			// 출발점으로 돌아오는 최소 비용 갱신
			
			// 출발점으로 돌아가지 못하는 경우
			if(cost[curr][START] == INF) {
				tsp[curr][bitmask] = INVALID;
			} else {
				tsp[curr][bitmask] = cost[curr][START];
			}
			return tsp[curr][bitmask];
		}
		
		// 이미 탐색을 수행했다면 생략
		if(tsp[curr][bitmask] != INF) return tsp[curr][bitmask];
		
		// 현재 위치에서 하나씩 더하며 탐색 수행
		int next;
		for(int i = 0; i < N; i++) {		
			next = bitmask | (1 << i);
			// 이미 방문한 경우 생략
			if(next == bitmask) continue;
			
			// 다음 점까지의 거리 + 다음 점에 방문하고 남은 점을 최적 경로로 돌았을 때의 거리가 최소가 되는 값 탐색
			tsp[curr][bitmask] = Math.min(tsp[curr][bitmask], dfs(i, next, N, cost, tsp) + cost[curr][i]);
		}
		
		return tsp[curr][bitmask];
	}
	
	// 도시 별 이동하는 비용 인접 행렬 리턴
	public static double[][] getCost(int N, Coord[] cities) {
		double[][] adjMat = new double[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				adjMat[i][j] = cities[i].getDistance(cities[j]);
			}
		}
		
		return adjMat;
	}

	public static class Coord {
		double x, y;
		
		public Coord(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double getDistance(Coord obj) {	
			return Math.sqrt(Math.pow(obj.x - this.x, 2) + Math.pow(obj.y - this.y, 2));
		}
	}
}
