# Dify学习文档

windows操作系统需要安装docker运行环境

从github拉取代码

```
https://github.com/langgenius/dify.git
```



代码拉完之后进入到对应的docker目录下修改对应的.env.example文件，重命名为.env

![image-20250221144714228](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250221144714228.png)





在对应目录下执行命令安装依赖

```
docker compose up -d
```





Dify和Coze的区别

![image-20250227121145237](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250227121145237.png)



**智能体：智能体是基于对话的 AI 项目，它通过对话方式接收用户的输入，由大模型自动调用插件或工作流等方式执行用户指定的业务流程，并生成最终的回复。智能客服、虚拟伴侣、个人助理、英语外教都是智能体的典型应用场景。**









# 提示工程

提示工程是 AI Agent 的核心，好的提示词能够大大提升大模型输出的准确性。

- 一个好的提示词能帮助 AI Agent 准确地理解任务，提高大模型的输出质量。
- 一个好的提示词可以减少 token 的消耗，降低成本。
- 一个好的提示词可以帮助 AI Agent 理解上下文，确保对话的连贯性。

因此，我们需要掌握如何编写有效的提示词。

- 什么是 CRISPE 框架？
- 什么是 BROKE 框架？
- 什么是 ICIO 框架？
- 什么是 CoT（思维链）？

我们还需要了解与大模型交互的规则，例如：

- 一篇长文分多次输出比一次性输出的质量更高。
- 使用不同的符号将不同信息分隔开，可以增强大模型的理解。
- 给出示例能帮助大模型快速理解你的要求。
- 对于复杂任务，将其拆解为若干步骤，引导大模型分步执行，效果更佳。
- 明确输出内容的限定，如字数、格式、风格、语言难度等。

**ICIO 框架：**

-  Intruction（任务）：明确指出希望 AI 执行的具体任务，如“翻译一段文本”或“撰写一篇关于 AI 伦理的博客文章”。
- Context（背景）：提供任务的背景信息，帮助 AI 理解任务的上下文，例如，“这段文本是用于公司内部会议的开场白”。
- Input Data（输入数据）：指定 AI 需要处理的具体数据，如“请翻译以下句子：‘人工智能正在改变世界’”。
- Output Indicator（输出格式）：设定期望的输出格式和风格，例如，“请以正式的商务英语风格翻译”。

**BROKE 框架：**

- Background（背景）：例如，“你正在为一家初创科技公司撰写一篇关于其最新产品的新闻稿。”
- Role（角色）：指定 AI 作为“新闻稿撰写者”，以便它能够以专业的角度回答问题。
- Objectives（目标/任务）：给出任务描述，如“撰写一篇吸引人的新闻稿，突出产品的独特卖点。”
- Key Result（关键结果）：设定回答的关键结果，例如，“使用正式和专业的语言，包含产品的主要功能和市场定位。”
- Evolve（改进）：在 AI 给出回答后，提供三种改进方法，如“调整语言风格以吸引目标受众”，“增加产品使用案例”，或“优化结构以提高阅读流畅性”。

**CRISPE 框架：**

1.  Capacity and Role（角色）：明确 AI 在交互中应扮演的角色，如教育者、翻译者或顾问。
2. Insight（背景）：提供角色扮演的背景信息，帮助 AI 理解其在特定情境下的作用。
3. Statement（任务）：直接说明 AI 需要执行的任务，确保其理解并执行用户的请求。
4.  Personality（格式）：设定 AI 回复的风格和格式，使其更符合用户的期望和场景需求。
5. Experiment（实验）：如果需要，可以要求 AI 提供多个示例，以供用户选择最佳回复。

**CoT 框架：**

Chain-of-Thought，一种引导大模型像人类一样，按照顺序一步步思考问题解决方案的方法。

主要包括 **Few-Shot CoT** 和 **Zero-Shot CoT** 两种应用方式。

Few-Shot CoT（少量示例）

描述思考步骤，先理解客户需求，再考虑<目标>，最后给出推荐并解释原因。

同时提供示例，这些示例展示 AI 如何依思维链思考给出答案。

Zero-Shot CoT（没有示例）

简单地增加一句提示词即可：

让我们一步步的思考（Let’s think step by step）





系统提示词参考
![image-20250403144830089](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250403144830089.png) 

用户提示词1

![image-20250403144852723](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250403144852723.png)

用户提示词2

![image-20250403144911749](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250403144911749.png)



















































































![image-20250304164344744](C:/Users/97151/AppData/Roaming/Typora/typora-user-images/image-20250304164344744.png)



























