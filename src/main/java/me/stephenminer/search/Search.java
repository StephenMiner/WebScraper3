package me.stephenminer.search;

public interface Search<T>{

    public T searchData();

    /**
     * This method should be called in order to conduct a search of a Google Classroom class
     * and populate some kind of data structure or make some kind of output as aligns with
     * T from search data
     */
    public void search();

}
