package com.nao20010128nao.NCL;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkClassLoader extends ClassLoader {
	Set<String> resources = new HashSet<>();
	String baseAddress;
	boolean delegate;
	ResourceConverter converter;
	ManifestLoader mLoader;
	Map<String, Class<?>> cache = new HashMap<>();

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			boolean delegate, ResourceConverter rc, ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(parent == null ? getSystemClassLoader() : parent);
		this.baseAddress = baseAddress;
		this.delegate = delegate;
		converter = rc == null ? ResourceConverter.NULL_CONVERTER : rc;
		mLoader = ml == null ? ManifestLoader.NULL_LOADER : ml;
		processML();
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			boolean delegate, ResourceConverter rc) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, delegate, rc, null);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			boolean delegate, ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, delegate, null, ml);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			ResourceConverter rc, ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, true, rc, ml);
	}

	public NetworkClassLoader(String baseAddress, boolean delegate,
			ResourceConverter rc, ManifestLoader ml) {
		this(baseAddress, null, delegate, rc, ml);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			boolean delegate) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, delegate, null, null);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			ResourceConverter rc) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, true, rc, null);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent,
			ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, true, null, ml);
	}

	public NetworkClassLoader(String baseAddress, boolean delegate,
			ResourceConverter rc) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, delegate, rc, null);
	}

	public NetworkClassLoader(String baseAddress, boolean delegate,
			ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, delegate, null, ml);
	}

	public NetworkClassLoader(String baseAddress, ResourceConverter rc,
			ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, true, rc, ml);
	}

	public NetworkClassLoader(String baseAddress, ClassLoader parent) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, parent, true, null, null);
	}

	public NetworkClassLoader(String baseAddress, boolean delegate) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, delegate, null, null);
	}

	public NetworkClassLoader(String baseAddress, ResourceConverter rc) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, true, rc, null);
	}

	public NetworkClassLoader(String baseAddress, ManifestLoader ml) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, true, null, ml);
	}

	public NetworkClassLoader(String baseAddress) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(baseAddress, null, true, null, null);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		if (delegate)
			return super.loadClass(name, resolve);
		else {
			try {
				return findClass(name);
			} catch (Throwable ex) {

			}
			try {
				return getParent().loadClass(name);
			} catch (Throwable ex) {
				throw new ClassNotFoundException(null, ex);
			}
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		if (cache.containsKey(name))
			return cache.get(name);
		String path = combinePath(baseAddress, name.replace('.', '/'))
				+ ".class";
		byte[] buf;
		try {
			Class<?> c = defineClass(name, buf = download(path), 0, buf.length);
			addResource(name.replace('.', '/') + ".class");
			cache.put(name, c);
			return c;
		} catch (Throwable ex) {
			// TODO 自動生成された catch ブロック
			throw new ClassNotFoundException("failed to load " + name, ex);
		}
	}

	@Override
	protected URL findResource(String name) {
		// TODO 自動生成されたメソッド・スタブ
		String path = combinePath(baseAddress, name);
		try {
			return addResource(new URL(path), name);
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			return null;
		}
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		return new Enumeration<URL>() {
			Iterator<String> base = resources.iterator();

			@Override
			public URL nextElement() {
				// TODO 自動生成されたメソッド・スタブ
				try {
					return new URL(combinePath(baseAddress, base.next()));
				} catch (MalformedURLException e) {
					// TODO 自動生成された catch ブロック
					return null;
				}
			}

			@Override
			public boolean hasMoreElements() {
				// TODO 自動生成されたメソッド・スタブ
				return base.hasNext();
			}
		};
	}

	private byte[] download(String combinedName) throws MalformedURLException,
			IOException {
		InputStream is = new URL(combinedName).openStream();
		is = converter.convert(is, combinedName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int r = 0;
		byte[] buf = new byte[10000];
		while (true) {
			r = is.read(buf);
			if (r <= 0)
				break;
			bos.write(buf, 0, r);
		}
		return bos.toByteArray();
	}

	static String combinePath(String main, String dir) {
		String trimmedMain = main;
		while (trimmedMain.endsWith("/") | trimmedMain.endsWith("\\"))
			trimmedMain = trimmedMain.substring(0, trimmedMain.length() - 1);
		String trimmedDir = dir;
		while (trimmedDir.startsWith("/") | trimmedDir.endsWith("\\"))
			trimmedDir = trimmedDir.substring(1);
		return (trimmedMain + "/" + trimmedDir).replace('\\', '/');
	}

	private void addResource(String path) {
		synchronized (resources) {
			resources.add(path);
		}
	}

	private void processML() {
		String source = null;
		try {
			source = mLoader.getSource(baseAddress);
		} catch (IOException e) {
			return;
		}
		List<String> files = null;
		try {
			InputStream is = new URL(source).openStream();
			files = mLoader.getFilesList(new BufferedReader(
					new InputStreamReader(is)), is, source);
			for (String e : files)
				resources.add(e);
		} catch (IOException e) {
			return;
		}
	}

	private URL addResource(URL url, String rawPath) {
		InputStream is = null;
		try {
			is = url.openStream();
			is.read(new byte[10]);// it's for test!
			addResource(rawPath);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return url;
	}
}
