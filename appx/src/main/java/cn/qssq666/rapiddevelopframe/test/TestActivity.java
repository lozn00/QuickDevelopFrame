package cn.qssq666.rapiddevelopframe.test;

import android.os.Bundle;

import cn.qssq666.rapiddevelopframe.base.activity.BaseActivity;

/**
 * Created by luozheng on 2016/11/4.  qssq666.cn Administrator
 */

/*

meta: {
    title: hexo.config.title,
    subtitle: hexo.config.subtitle,
    description: hexo.config.description,
    author: hexo.config.author,
    url: hexo.config.url
},
pages: [{ //-> all pages
    title: page.title,
    slug: page.slug,
    date: page.date,
    updated: page.updated,
    comments: page.comments,
    permalink: page.permalink,
    path: page.path,
    excerpt: page.excerpt, //-> only text ;)
    keywords: null //-> it needs settings
    text: page.content, //-> only text minified ;)
    raw: page.raw, //-> original MD content
    content: page.content //-> final HTML content
}],
posts: [{ //-> only published posts
    title: post.title,
    slug: post.slug,
    date: post.date,
    updated: post.updated,
    comments: post.comments,
    permalink: post.permalink,
    path: post.path,
    excerpt: post.excerpt, //-> only text ;)
    keywords: null //-> it needs settings
    text: post.content, //-> only text minified ;)
    raw: post.raw, //-> original MD content
    content: post.content, //-> final HTML content
    categories: [{
        name: category.name,
        slug: category.slug,
        permalink: category.permalink
    }],
    tags: [{
        name: tag.name,
        slug: tag.slug,
        permalink: tag.permalink
    }]
}]
 */


public class TestActivity extends BaseActivity {
    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutID() {
        return 0;
    }
}
