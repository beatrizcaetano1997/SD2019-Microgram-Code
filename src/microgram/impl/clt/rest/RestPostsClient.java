package microgram.impl.clt.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import microgram.api.Post;
import microgram.api.java.Posts;
import microgram.api.java.Result;
import microgram.api.rest.RestPosts;

//TODO Make this class concrete
public abstract class RestPostsClient extends RestClient implements Posts {

	public RestPostsClient(URI serverUri) {
		super(serverUri, RestPosts.PATH);
	}

	
	public Result<String> createPost(Post post) {
		Response r = target
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post( Entity.entity( post, MediaType.APPLICATION_JSON));
		
		return super.responseContents(r, Status.OK, new GenericType<String>(){});	
	}
	
	public Result<Post> getPost( String postId ) {
		Response r = target.path(postId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<Post>(){});
	}
	
	public Result<Void> deletePost( String postId ) {
		Response r = target.path(postId)
				.request()
				.delete();
		return super.responseContents(r, Status.OK, new GenericType<Void>(){});
	}
	
	public Result<Void> like(String postId, String userId, boolean isLiked) {
		Response r = target.path(userId + "/likes/" + postId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.put( Entity.entity(isLiked, MediaType.APPLICATION_JSON));
		return super.responseContents(r, Status.OK, new GenericType<Void>(){});
		
	}
	
	public Result<Boolean> isLiked(String postId, String userId) {
		Response r = target.path(postId + "/likes/" + userId)
				.request()
				.get();
		return super.responseContents(r, Status.OK, new GenericType<Boolean>(){});
	}
	
	public Result<List<String>> getPosts( String userId) {
		Response r = target.path("/from/" + userId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>(){});	
	}
	
	public Result<List<String>> getFeed( String userId) {
		Response r = target.path("/feed/" + userId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>(){});
	}
	
	
}
