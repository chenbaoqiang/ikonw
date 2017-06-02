package com.feinno.serialization.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.feinno.serialization.protobuf.generator.ProtoConfig;
import com.feinno.serialization.protobuf.log.Logger;
import com.feinno.serialization.protobuf.log.LoggerFactory;

/**
 * <b>描述: </b>序列化组件对外暴露的统一序列化接口
 * <p>
 * <b>功能：</b>将Java对象中的数据转换为符合Google
 * protobuf二进制存储协议的二进制流,或从符合该协议的二进制流中解析出Java对象中的数据,用在序列化及反序列化的场景中
 * ,相对于Java原生序列化的优势是可以使用在异构语言的环境中，且速度及效率要高于Java原生的序列化.
 * <p>
 * <b>用法: </b>
 * 
 * <pre>
 *  序列化一个String对象
 *  byte[] buffer = ProtoManager.toByteArray("Feinno");
 *  
 *  反序列化一个String对象
 *  String newString = "";
 *  newString = ProtoManager.parseFrom(newString,buffer);
 *  System.out.println(newString);
 *  
 *  序列化一个List对象
 *  List<String> stringList = new ArrayList<String>();
 *  stringList.add("Feinno");
 *  stringList.add("Good");
 *  buffer = ProtoManager.toByteArray(stringList,String.class);
 *  
 *  反序列化一个List对象
 *  List<String> resultList = new ArrayList<String>();
 *  ProtoManager.parseFrom(buffer, resultList, String.class);
 *  System.out.println(resultList.getSize());
 * </pre>
 * 
 * 序列化或反序列化一个混合对象，对象中有1个String和1个int，需要新创建一个类，且继承自{@link ProtoEntity}，请点击参考
 * {@link ProtoEntity}的javadoc
 * <p>
 * <b>支持范围: </b>ProtoManager支持的类型范围为 {@link ProtoFieldType}中标识的除 {@link Object}
 * 外的全部类型,以及这些类型的混合情况
 * 
 * @author Lv.Mingwei
 * 
 */
public final class ProtoManager {

	/**
	 * 开启此DEBUG后,会有如下变化<br>
	 * 1.生成源码，默认存储在根路径下<br>
	 * 2.在序列化出错时，生成详细的异常信息，打印源码中出错位置的代码
	 */
	private static boolean IS_DEBUG = false;

	/**
	 * 是否刷新缓存，如果为是，当找不到ProtobufBuilder时，覆盖式创建，而不再尝试获取直接获取
	 */
	private static boolean IS_REFLUSH_CACHE = false;

	/**
	 * class得缓存路径，如果该路径不为null，则生成得class将存储在该路径下
	 */
	private static String TEMP_CLASS_SAVE_PATH = null;

	/** 当开启Debug模式时，源文件默认保存路径 */
	public static String SOURCE_SAVE_PATH = null;

	private ProtoManager() {

	}

	/**
	 * 将对象序列化成符合ProtoBuf格式的byte
	 * 
	 * @param args
	 * @return
	 * @throws IOException
	 */
	public static <T extends Object> byte[] toByteArray(T t) throws IOException {
		if (t instanceof ProtoEntity) {
			return ProtoBuilderHelper.getProtoBuilder((ProtoEntity) t).toByteArray();
		}
		throw new RuntimeException("Unsupported " + t.getClass() + " serialization.");
	}

	/**
	 * 将对象列化到流中
	 * 
	 * @param t
	 * @param output
	 * @return
	 * @throws IOException
	 */
	public static <T extends Object> void writeTo(OutputStream output, T t) throws IOException {
		if (t instanceof ProtoEntity) {
			ProtoBuilderHelper.getProtoBuilder((ProtoEntity) t).writeTo(output);
			return;
		}
		throw new RuntimeException("Unsupported " + t.getClass() + " serialization.");
	}

	/**
	 * 从input中解析出并保存成需要的格式
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T parseFrom(InputStream input, T t) throws IOException {
		if (t instanceof ProtoEntity) {
			ProtoEntity protoEntity = (ProtoEntity) t;
			ProtoBuilderHelper.getProtoBuilder(protoEntity).parseFrom(input);
			return (T) protoEntity;
		}
		throw new RuntimeException("Unsupported " + t.getClass() + " serialization.");
	}

	/**
	 * 从byte数组中解析出并保存成需要的格式
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T parseFrom(byte[] buffer, T t) throws IOException {
		if (t instanceof ProtoEntity) {
			ProtoEntity protoEntity = (ProtoEntity) t;
			ProtoBuilderHelper.getProtoBuilder(protoEntity).parseFrom(buffer);
			return (T) protoEntity;
		}
		throw new RuntimeException("Unsupported " + t.getClass() + " serialization.");
	}

	public static <T extends ProtoEntity> ProtoBuilder<T> getProtoBuilder(T t) {
		return ProtoBuilderHelper.getProtoBuilder(t);
	}

	public static boolean isDebug() {
		return IS_DEBUG;
	}

	public static void setDebug(boolean isDebug) {
		ProtoManager.IS_DEBUG = isDebug;
	}

	public static boolean isReflushCache() {
		return IS_REFLUSH_CACHE;
	}

	public static void setReflushCache(boolean isReflushCache) {
		ProtoManager.IS_REFLUSH_CACHE = isReflushCache;
	}

	public static String getTempClassSavePath() {
		return TEMP_CLASS_SAVE_PATH;
	}

	public static void setTempClassSavePath(String classSavePath) {
		TEMP_CLASS_SAVE_PATH = classSavePath;
	}

}

/**
 * 这个类是用于管理与生成ProtoBuilder和ProtoBuilderFactory的类<br>
 * 用户会调用{@link ProtoHelper# getProtoBuilder(T t)}
 * 方法从ProtoHelper中取某一个T对应的ProtoBuilder,
 * ProtoBuilderHelper会从自身的PROTO_BUILDER_FACTORY_MAP中寻找这个ProtoBuilder的Factory
 * ,如果找到了，那么通过这个Factory来创建对应的ProtoBuilder ，如果发现没有这个类对应的Factory ，会调用
 * {@link ProtoBuilderCodeGenerator}
 * 生成ProtoBuilderFactory和ProtoBuilder源码，拿到源码后，此类调用JavaEval的编译源码方法，将源码编译后
 * ，存入PROTO_BUILDER_FACTORY_MAP中，以备下次使用，并将需要的对象返回.
 * 
 * @author Lv.Mingwei
 * 
 */
class ProtoBuilderHelper {

	private static final Logger logger = LoggerFactory.getLogger(ProtoBuilderHelper.class);

	/** 这里保存了所有继承自ProtoEntity的ProtoBuilderFactory */
	private static final Map<Class<?>, ProtoBuilderFactory> PROTO_BUILDER_FACTORY_MAP = Collections
			.synchronizedMap(new HashMap<Class<?>, ProtoBuilderFactory>());

	/** 这个是用于锁住同时只有一个线程能够进行自动构建源码与编译 */
	private static final Object SYNCHRONIZED_OBJECT = new Object();

	/**
	 * 通过待序列化的对象获得其对应的ProtoBuilder，<br>
	 * 获得ProtoBuilder的步骤是首先从ProtoManage自己维护的MAP中取得对应ProtoBuilder的Factory，
	 * 通过这个Factory来创建ProtoBuilder，如果这个Factory不存在，则需要生成这个Factory和Builder
	 * 
	 * @param t
	 * @return
	 */
	static <T extends ProtoEntity> ProtoBuilder<T> getProtoBuilder(T t) {
		ProtoBuilderFactory factory = PROTO_BUILDER_FACTORY_MAP.get(t.getClass());
		if (factory == null) {
			logger.info("Not found [{}] ProtoBuilder and ProtoBuilderFactory. Ready to automatically generated.", t
					.getClass().getName());
			// 好吧，这个待序列化对象的类型没有找到对应的BuilderFactory，那么在这里在构建一个BuilderFactory，构建的过程就是代码自动生成与编译的过程
			synchronized (SYNCHRONIZED_OBJECT) {
				long startTime = System.nanoTime();
				if (PROTO_BUILDER_FACTORY_MAP.get(t.getClass()) == null) {
					// 如果没有开启刷新缓存，那么则先从当前类路径中直接取得，当取不到时再创建
					factory = getProtoBuilderFactory(t.getClass());
					if (factory == null) {
						throw new RuntimeException(String.format("Not found %s ProtobufferBuiler.",t.getClass()));
					}
					PROTO_BUILDER_FACTORY_MAP.put(t.getClass(), factory);
					if (logger.isInfoEnabled()) {
						logger.info("The end of the generated  ProtoBuilder and ProtoBuilderFactory source code.", t
								.getClass().getName());
						logger.info("Create {}ProtoBuilder.class with {} ms.", t.getClass().getSimpleName(),
								java.util.concurrent.TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime,
										java.util.concurrent.TimeUnit.NANOSECONDS));
					}

				} else {
					// 如果阻塞过后已经存在了这个对象，则直接将引用赋予到factory变量上，用于后面的创建ProtoBuilder
					factory = PROTO_BUILDER_FACTORY_MAP.get(t.getClass());
					logger.info(
							"OK, Found [{}] ProtoBuilder and ProtoBuilderFactory. Do not need to automatically generated.",
							t.getClass().getName());
				}
			}
		}
		return factory.newProtoBuilder(t);
	}

	/**
	 * 从当前classLoader中获取ProtoBuilderFactory
	 * 
	 * @param clazz
	 * @return
	 */
	static ProtoBuilderFactory getProtoBuilderFactory(Class<?> clazz) {
		try {
			ProtoBuilderFactory factory = newClassInstance(clazz.getClassLoader(), ProtoBuilderFactory.class,
					getBuilderFactoryClassFullName(clazz));
			return factory;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获得一个proto类的工厂类名称(工厂类的全路径)
	 * 
	 * @param clazz
	 * @return
	 */
	static String getBuilderFactoryClassFullName(Class<?> clazz) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getBuilderPackage(clazz)).append(".").append(clazz.getSimpleName())
				.append(ProtoConfig.PROTO_BUILDER_FACTORY_NAME);
		return stringBuilder.toString();
	}

	/**
	 * 获得一个序列化辅助类所属的包路径
	 * 
	 * @param clazz
	 * @return
	 */
	static String getBuilderPackage(Class<?> clazz) {
		if (clazz.getPackage() != null) {
			return clazz.getPackage().getName();
		} else {
			return ProtoConfig.PACKAGE_CODE;
		}
	}

	/**
	 * 创建一个指定类路径的实例，此类路径为全路径
	 * 
	 * @param classLoader
	 *            此对象的类加载器
	 * @param clazz
	 * @param classPath
	 * @return
	 */
	static <T> T newClassInstance(ClassLoader classLoader, Class<T> clazz, String classPath) {
		try {
			Class<?> classTemp = Class.forName(classPath, true, classLoader);
			@SuppressWarnings("unchecked")
			T instance = (T) classTemp.newInstance();
			return instance;
		} catch (Exception e) {
			throw new RuntimeException("JavaEval.newClassInstance()  found error:", e);
		}
	}
}
