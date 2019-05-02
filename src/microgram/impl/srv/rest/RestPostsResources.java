package microgram.impl.srv.rest;

import java.net.URI;
import java.util.List;

import microgram.api.Post;
import microgram.api.java.Posts;
import microgram.api.rest.RestPosts;
import microgram.impl.srv.java.JavaPosts;

// Make this class concrete.
public abstract class RestPostsResources extends RestResource implements RestPosts {

	final Posts impl;
		
	public RestPostsResources(URI serverUri) {
		this.impl = new JavaPosts();
	}
	
	@Override
	public Post getPost(String postId) {
		return super.resultOrThrow(impl.getPost(postId));
	}
	
	public String createPost(Post post) {
		return super.resultOrThrow(impl.createPost(post));
	}
	
	public void deletePost( String postId ) {
		super.resultOrThrow(impl.deletePost(postId));
	}
	
	public void like(String postId, String userId, boolean isLiked) {
		super.resultOrThrow(impl.like(postId, userId, isLiked));
	}
	
	public boolean isLiked(String postId, String userId) {
		return super.resultOrThrow(impl.isLiked(postId, userId));
	}
	
	public List<String> getPosts( String userId) {
		return super.resultOrThrow(impl.getPosts(userId));
	}
	
	public List<String> getFeed( String userId) {
		return super.resultOrThrow(impl.getFeed(userId));
	}
 
}
