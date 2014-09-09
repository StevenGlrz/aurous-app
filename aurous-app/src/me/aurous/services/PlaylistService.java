package me.aurous.services;


public interface PlaylistService {
	
	public void buildPlaylist(String url, String playlistName);
	
	public String buildPlayListLine(String context);
	
	public String getName();
}
