package com.nao20010128nao.NCL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ManifestLoader {

	public ManifestLoader() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public abstract List<String> getFilesList(BufferedReader reader,
			InputStream original, String source) throws IOException;

	public abstract String getSource(String baseAddress) throws IOException;

	public abstract void setSource(String dirName) throws IOException;

	public static final ManifestLoader NULL_LOADER = new ManifestLoader() {

		@Override
		public String getSource(String baseAddress) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			throw new IOException("dummy error");
		}

		@Override
		public List<String> getFilesList(BufferedReader reader,
				InputStream original, String source) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			return Collections.emptyList();
		}

		@Override
		public void setSource(String dirName) throws IOException {
			// TODO 自動生成されたメソッド・スタブ

		}
	};

	public static final ManifestLoader JAVA_MANIFEST_LOADER = new ManifestLoader() {
		@Override
		public List<String> getFilesList(BufferedReader reader,
				InputStream original, String source) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			ArrayList<String> list = new ArrayList<String>();
			String line = null;
			while (true) {
				line = reader.readLine();
				if (line == null)
					break;
				if (line.startsWith("Name: "))
					list.add(line.substring(6));
			}
			return list;
		}

		@Override
		public String getSource(String baseAddress) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			return NetworkClassLoader.combinePath(baseAddress,
					"META-INF\\MANIFEST.MF");
		}

		@Override
		public void setSource(String dirName) throws IOException {
			// TODO 自動生成されたメソッド・スタブ

		}
	};

	public static class SimpleLinesLoader extends ManifestLoader {
		String dir;

		public SimpleLinesLoader(String dir) {
			this.dir = dir;
		}

		@Override
		public List<String> getFilesList(BufferedReader reader,
				InputStream original, String source) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			ArrayList<String> list = new ArrayList<String>();
			String line = null;
			while (true) {
				line = reader.readLine();
				if (line == null)
					break;
				list.add(line);
			}
			return list;
		}

		@Override
		public String getSource(String baseAddress) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			return NetworkClassLoader.combinePath(baseAddress, dir);
		}

		@Override
		public void setSource(String dirName) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			dir = dirName;
		}
	}
}
