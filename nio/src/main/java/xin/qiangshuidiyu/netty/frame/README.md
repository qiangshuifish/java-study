### TCP 协议的拆包和粘包

前面在 aio,bio,nio,aio,以及最简单的netty服务器中，
我们都发现一个问题，就是服务器在接受到客服端的消息后，
回复客服端消息时，原本的消息是：
  接受服务器数据 : 客服端你好！收到的数据为｛你好服务器｝
但是客服端收到的确实
  接受服务器数据 : 客服端你好！收到的数据为｛你好服务器
少了一个“｝”，这就是TPC的拆包导致的原因

TCP的拆包粘包问题需要客服端和服务器共同配合解决！