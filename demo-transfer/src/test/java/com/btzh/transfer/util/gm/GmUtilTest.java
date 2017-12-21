package com.btzh.transfer.util.gm;

import com.btzh.BaseJunit4Test;

import java.io.File;

public class GmUtilTest extends BaseJunit4Test{

	@org.junit.Test
	public void testSend() throws Exception {
		/*Map<String, String> keyMap = GmUtil.generateKeyPair();
		String plaintext = "when you're gone";

		System.out.println("publickey length: " + keyMap.get("publickey").length());
		System.out.println("privatekey length: " + keyMap.get("privatekey").length());
		String ciphertext = GmUtil.encryptByPublickey(keyMap.get("publickey"), plaintext);
		System.out.println("公钥加密密文：" + ciphertext);
		System.out.println("私钥解密后的明文: " + GmUtil.decryptByPrivatekey(keyMap.get("privatekey"), ciphertext));*/

		
		// 测试生成文件摘要
		/*long temp3 = System.nanoTime();
		System.out.println(GmUtil.getSM3Digest(new File("/home/hgq/桌面/test/spring.pdf")));
		System.out.println("cost time: " + (System.nanoTime() - temp3));*/
		
		// 测试sm4加密文件
		String key = GmUtil.getRandomSm4Key();
		System.out.println("key: " + key +"\tkeylength:" + key.getBytes().length);
		String destDir = "/home/hgq/桌面/test/";
		
		File plaintextFile = new File("/home/hgq/桌面/bag.tar.gz");
		long temp = System.nanoTime();
		GmUtil.sm4EncryptFile(plaintextFile, destDir, key);
		System.out.println("加密时间： " + (System.nanoTime() - temp));
		
		File ciphertextFile = new File("/home/hgq/桌面/test/bag.tar.gz.sm4");
		long temp1 = System.nanoTime();
		GmUtil.sm4DecryptFile(ciphertextFile, destDir, key);
		System.out.println("解密时间： " + (System.nanoTime() - temp1));
		System.out.println("加密后文件长度计算值： " +  GmUtil.getSm4CipherTextLength(plaintextFile.length()) + ", 实际长度： " + ciphertextFile.length());
		
		// 测试sm4加密byte数组
		/*try {
			FileInputStream fis = new FileInputStream(plaintextFile);
			//FileOutputStream fos = new FileOutputStream("/home/hgq/桌面/test/spring.pdf.sm4");
			FileOutputStream fos1 = new FileOutputStream("/home/hgq/桌面/test/spring.pdf");
			byte[] b1 = new byte[1024];
			byte[] b2;
			int len = -1;
			long temp = System.nanoTime();
			while ((len=fis.read(b1)) > 0) {
				boolean isPadding = fis.available() == 0;
				if (isPadding) {
					b1 = Arrays.copyOf(b1, len);
				}
				b2 = GmUtil.sm4EncryptData(b1, key, isPadding);
				
				byte[] b3 = GmUtil.sm4DecryptData(b2, key, isPadding);
				
				fos1.write(b3);
				if (!Arrays.equals(b1, b3)) {
					System.out.println("len: " + len +", outlen: " + b3.length + ", isPadding: " + isPadding + ", istrue: " + Arrays.equals(b2, b3));
					System.out.println(Arrays.toString(b1));
					System.out.println(Arrays.toString(b2));
					System.out.println(Arrays.toString(b3));
				}
			}
			System.out.println("finish, cost time : "+ (System.nanoTime() - temp));
			fis.close();
			fos1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
}
