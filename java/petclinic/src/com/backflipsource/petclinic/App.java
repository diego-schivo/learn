package com.backflipsource.petclinic;

import javax.servlet.annotation.WebListener;

import com.backflipsource.Server;
import com.backflipsource.ui.EntityUIWebListener;

@WebListener
public class App extends EntityUIWebListener {

	protected static App app;

	public static App getApp() {
		return app;
	}

	public App() {
		app = this;
	}

	public static void main(String[] args) {
		new Server();
	}
}
