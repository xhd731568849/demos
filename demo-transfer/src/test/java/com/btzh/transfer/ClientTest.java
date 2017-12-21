package com.btzh.transfer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.btzh.Main;
import com.btzh.config.Config;
import com.btzh.transfer.entity.EncryptedFile;
import com.btzh.transfer.keyload.KeyLoader;


@SuppressWarnings("unused")
public class ClientTest {

	public static void main(String[] args) throws Exception {
		Thread.sleep(15000);
		final ClassPathXmlApplicationContext application = new ClassPathXmlApplicationContext(new String[] { "classpath*:applicationContext.xml" });
		Method method = Main.class.getDeclaredMethod("setApplicationContext", ApplicationContext.class);
		method.setAccessible(true);
		method.invoke(null, application);
		
		Config config = application.getBean(Config.class);

		Client client = new Client();


		ExecutorService executor = Executors.newFixedThreadPool(15);
		CountDownLatch latch = new CountDownLatch(14);
		
		//读取对方公钥
		new Thread(new KeyLoader()).start();

		/*executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				client.send(new Text(BusinessType.SQL,"hello world"));
				
				latch.countDown();
			}
		});*/

		// 发送加密文件
		for (int i = 0; i < 10; i++) {
			final int j = i;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1200L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					long start = System.nanoTime();
					client.send(new EncryptedFile("/pressure/bigfile" + j + ".tar.gz", new File("/home/hgq/桌面/test/bag.tar.gz")));
					System.out.println("send bigfile cost time:" + (System.nanoTime() - start));

					latch.countDown();
				}
			});
		}
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				client.send(new EncryptedFile("/pressure/docker", new File("/home/hgq/桌面/test/docker")));

				latch.countDown();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				client.send(new EncryptedFile("/pressure/dockersm3", new File("/home/hgq/桌面/test/dockersm3")));

				latch.countDown();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				client.send(new EncryptedFile("/pressure/倒霉熊150.mp4", new File("/home/hgq/桌面/test/倒霉熊150.mp4")));

				latch.countDown();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				long start = System.nanoTime();
				client.send(new EncryptedFile("/pressure/spring.pdf", new File("/home/hgq/桌面/test/spring.pdf")));
				System.out.println("send spring cost time:" + (System.nanoTime() - start));

				latch.countDown();
			}
		});

		/*executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				client.send(new BaseFile("/1/2/3/test.txt", new File("/home/tzb/logs/hello.txt")));

				latch.countDown();
			}
		});*/

		/*
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				client.send(new BaseFile(BusinessType.PUBLIC_KEY_FILE, new File("/Users/wanglidong/temp/xxx/hello1.txt")));
				
				latch.countDown();
			}
		});*/
		

		/*
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				File transFile = new File("/home/hgq/桌面/test/spring.pdf");
				String filePath = "transfered-" + transFile.getName();
				client.send(new EncryptedFile(filePath, transFile));

				latch.countDown();
			}
		});
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				File transFile = new File("/home/hgq/桌面/test/倒霉熊150.mp4");
				String filePath = "transfered-" + transFile.getName();
				client.send(new EncryptedFile(filePath, transFile));

				latch.countDown();
			}
		});*/
		
		/*executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				client.send(new DatabaseFieldFile("表名", "字段名", "ID", "pathOfDatabaseFieldFile", new File("/Users/wanglidong/temp/xxx/db.blob")));
				
				latch.countDown();
			}
		});*/

		final Server server = new Server();
		final long serverstart = System.nanoTime();
		//如果打断点调试，把下面这个线程注释掉
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					latch.await();
					System.out.println("传输任务执行完毕！");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				executor.shutdown();
				
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				application.close();
				server.shutdown();
				System.out.println("server start at: " + serverstart + "\t,end at: " + System.nanoTime() +"\t,during: " + (System.nanoTime() - serverstart));
				System.exit(0);
				
			}
		}).start();
		
		server.start();
	}
}