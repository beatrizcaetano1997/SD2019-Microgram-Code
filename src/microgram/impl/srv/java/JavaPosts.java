package microgram.impl.srv.java;

import static microgram.api.java.Result.error;
import static microgram.api.java.Result.ok;
import static microgram.api.java.Result.ErrorCode.CONFLICT;
import static microgram.api.java.Result.ErrorCode.NOT_FOUND;
import static microgram.api.java.Result.ErrorCode.NOT_IMPLEMENTED;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import discovery.Discovery;
import microgram.api.Post;
import microgram.api.java.Posts;
import microgram.api.java.Result;
import microgram.api.java.Result.ErrorCode;
import microgram.impl.clt.rest.RestProfilesClient;
import utils.Hash;

public class JavaPosts implements Posts {

	protected Map<String, Post> posts = new ConcurrentHashMap<>();
	protected Map<String, Set<String>> likes = new ConcurrentHashMap<>();
	protected Map<String, Set<String>> userPosts = new ConcurrentHashMap<>();

	@Override
	public Result<Post> getPost(String postId) {
		Post res = posts.get(postId);
		if (res != null)
			return ok(res);
		else
			return error(NOT_FOUND);
	}

	@Override
	public Result<Void> deletePost(String postId) {
		if (posts.get(postId) != null) {
			userPosts.get((posts.get(postId)).getOwnerId()).remove(postId);
			posts.remove(postId);
			likes.remove(postId);
			return ok();
		} else
			return Result.error(ErrorCode.NOT_FOUND);
	}

	@Override
	public Result<String> createPost(Post post) {
		String postId = Hash.of(post.getOwnerId(), post.getMediaUrl());
		if (posts.putIfAbsent(postId, post) == null) {

			likes.put(postId, new HashSet<>());

			Set<String> posts = userPosts.get(post.getOwnerId());
			if (posts == null)
				userPosts.put(post.getOwnerId(), posts = new LinkedHashSet<>());

			posts.add(postId);
		}
		return ok(postId);
	}

	@Override
	public Result<Void> like(String postId, String userId, boolean isLiked) {

		Set<String> res = likes.get(postId);
		if (res == null)
			return error(NOT_FOUND);

		if (isLiked) {
			if (!res.add(userId))
				return error(CONFLICT);
		} else {
			if (!res.remove(userId))
				return error(NOT_FOUND);
		}

		getPost(postId).value().setLikes(res.size());
		return ok();
	}

	@Override
	public Result<Boolean> isLiked(String postId, String userId) {
		Set<String> res = likes.get(postId);

		if (res != null)
			return ok(res.contains(userId));
		else
			return error(NOT_FOUND);
	}

	@Override
	public Result<List<String>> getPosts(String userId) {
		Set<String> res = userPosts.get(userId);
		if (res != null)
			return ok(new ArrayList<>(res));
		else
			return error(NOT_FOUND);
	}

	@Override
	public Result<List<String>> getFeed(String userId) {
		URI[] servers;
		List<String> feedList = new ArrayList<String>();

		try {
			servers = Discovery.findUrisOf("Microgram-Profiles", 1);
			RestProfilesClient clientFeed = new RestProfilesClient(servers[0]) {
			};

			if (clientFeed.getProfile(userId).isOK()) {
				for (String id : userPosts.keySet()) {
					Result<Boolean> r = clientFeed.isFollowing(userId, id);
					if (r.value()) {
						for (String postid : userPosts.get(id)) {
							feedList.add(postid);
						}
					}
				}
			} else {
				return error(NOT_FOUND);
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return ok(feedList);
	}
}
