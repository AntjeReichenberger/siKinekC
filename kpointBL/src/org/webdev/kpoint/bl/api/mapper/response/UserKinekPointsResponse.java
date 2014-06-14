package org.webdev.kpoint.bl.api.mapper.response;

import java.util.List;
import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint;

public class UserKinekPointsResponse {

	private int defaultKPId;
	private List<KinekPoint> favoriteKinekPoints;

	public void setDefaultKPId(int defaultKPId) {
		this.defaultKPId = defaultKPId;
	}

	public int getDefaultKPId() {
		return defaultKPId;
	}

	public void setFavoriteKinekPoints(List<KinekPoint> favoriteKinekPoints) {
		this.favoriteKinekPoints = favoriteKinekPoints;
	}

	public List<KinekPoint> getFavoriteKinekPoints() {
		return favoriteKinekPoints;
	}
}
