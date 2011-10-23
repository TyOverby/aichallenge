package com.prealpha.aichallenge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.prealpha.aichallenge.protocol.Bot;
import com.prealpha.aichallenge.protocol.Order;
import com.prealpha.aichallenge.protocol.Point;

final class MyBot extends Bot {
	private final Map<Point, Ant> ants;

	private MyBot() {
		ants = new HashMap<Point, Ant>();
	}

	@Override
	protected void beforeUpdate() {
		for (Order order : getOrders()) {
			Point origin = order.getOrigin();
			Point target = order.getTarget(getMap());
			Ant ant = ants.get(origin);
			ant.orderConfirmed();
			ants.remove(origin);
			ants.put(target, ant);
		}
		super.beforeUpdate();
	}

	@Override
	protected void addAnt(int row, int col, int owner) {
		if (owner == 0) {
			Point point = new Point(row, col);
			if (!ants.containsKey(point)) {
				Ant ant = new Scout(getMap(), point);
				ants.put(point, ant);
			}
		}
		super.addAnt(row, col, owner);
	}

	@Override
	protected void removeAnt(int row, int col, int owner) {
		if (owner == 0) {
			Point point = new Point(row, col);
			ants.remove(point);
		}
		super.removeAnt(row, col, owner);
	}

	@Override
	protected void doTurn() {
		for (Ant ant : ants.values()) {
			Order order = ant.getOrder();
			if (order != null) {
				issueOrder(order);
			}
		}
	}

	public static void main(String... args) throws IOException {
		new MyBot().readSystemInput();
	}
}
