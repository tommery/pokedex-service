package com.sita.dto;

import java.util.List;

public class EvolutionDto {
	private List<String> prev;
    private List<List<String>> next;

    public List<String> getPrev() { return prev; }
    public void setPrev(List<String> prev) { this.prev = prev; }

    public List<List<String>> getNext() { return next; }
    public void setNext(List<List<String>> next) { this.next = next; }
}
