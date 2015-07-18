package com.nao20010128nao.NCL;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NCLPerformanceCounter {
	static Map<NetworkClassLoader, NCLPerformanceCounter> instances = new HashMap<NetworkClassLoader, NCLPerformanceCounter>();

	NetworkClassLoader classLoader;
	long downloadedBytes, downloadedFiles, downloadElapsed;
	long loadedClassElapsed;

	private NCLPerformanceCounter(NetworkClassLoader classLoader) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.classLoader = classLoader;
	}

	public static NCLPerformanceCounter getFor(NetworkClassLoader classLoader) {
		Objects.requireNonNull(classLoader);
		if (instances.containsKey(classLoader))
			return instances.get(classLoader);
		else {
			NCLPerformanceCounter instance = new NCLPerformanceCounter(
					classLoader);
			instances.put(classLoader, instance);
			return instance;
		}
	}

	void increaseDB(long amount) {
		downloadedBytes += amount;
	}

	void increaseDF(long amount) {
		downloadedFiles += amount;
	}

	void increaseDE(long amount) {
		downloadElapsed += amount;
	}

	void increaseLCE(long amount) {
		loadedClassElapsed += amount;
	}

	public long getDownloadedBytes() {
		return downloadedBytes;
	}

	public long getDownloadedFiles() {
		return downloadedFiles;
	}

	public long getDownloadElapsed() {
		return downloadElapsed;
	}

	public long getLoadedClasses() {
		return classLoader.getCacheSize();
	}

	public long getLoadedClassElapsed() {
		return loadedClassElapsed;
	}

	public double getAverageDownloadSpeed() {
		return (downloadedBytes / 1024) / (downloadElapsed / 1000);
	}

	public double getAverageDownloadedFileSize() {
		return downloadedBytes / downloadedFiles;
	}
}
