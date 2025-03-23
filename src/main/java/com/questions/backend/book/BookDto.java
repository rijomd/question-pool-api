package com.questions.backend.book;

public class BookDto {
    private Integer id;
    private String name;
    private Integer pageCount;
    private Integer authorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "BookDto [id=" + id + ", name=" + name + ", pageCount=" + pageCount + ", authorId=" + authorId + "]";
    }
}
