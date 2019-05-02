package microgram.impl.clt.java;

import java.util.List;

import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.java.Result;

public abstract class RetryProfilesClient extends RetryClient implements Profiles {

	final Profiles impl;

	public RetryProfilesClient( Profiles impl ) {
		this.impl = impl;	
	}
	
	@Override
	public Result<Profile> getProfile(String userId) {
		return reTry( () -> impl.getProfile(userId));
	}
	
	public Result<Void> createProfile(Profile profile) {
		return reTry( () -> impl.createProfile(profile));
	}
	
	public Result<Void> deleteProfile(String userId) {
		return reTry( () -> impl.deleteProfile(userId));
	}
	
	public Result<List<Profile>> search(String prefix) {
		return reTry( () -> impl.search(prefix));
	}
	
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing) {	
		return reTry( () -> impl.follow(userId1, userId2, isFollowing));
	}
	
	public Result<Boolean> isFollowing(String userId1, String userId2) {
		return reTry( () -> impl.isFollowing(userId1, userId2));
	}
}
