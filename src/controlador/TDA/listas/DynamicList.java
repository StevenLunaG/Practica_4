/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.listas;

import controlador.TDA.listas.Exception.EmptyException;
import java.io.FileWriter;

/**
 *
 * @author santi
 */
public class DynamicList<E> {

    private Node<E> header;
    private Node<E> last;
    private Integer length;

    public DynamicList() {
        header = null;
        last = null;
        length = 0;
    }
    
    public Boolean merge(E data, Integer index) throws EmptyException {
        if (isEmpty()){
            throw new EmptyException("Vacio");
        }else{
            getNode(index).setInfo(data);
            return true;
        }
    }
    
    public E[] toArray(){
        Class clazz = null;
        E[] matriz = null;
        if (length > 0) {
            clazz = header.getInfo().getClass();
            matriz = (E[])java.lang.reflect.Array.newInstance(clazz, length);
            Node<E> aux = header;
            for (int i = 0; i < length; i++) {
                matriz[i] = aux.getInfo();
                aux = aux.getNext();
            }
        }
        return matriz;
    }
    
    public Node<E> getHeader() {
        return header;
    }

    public void setHeader(Node<E> header) {
        this.header = header;
    }

    public Node<E> getLast() {
        return last;
    }

    public void setLast(Node<E> last) {
        this.last = last;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean isEmpty() {
        return header == null || getLength() == 0;
    }

    private void addFirst(E info) {
        Node<E> help;
        if (isEmpty()) {
            help = new Node<>(info);
            header = help;
            last = help;

        } else {
            Node<E> headHelp = header;
            help = new Node<>(info, headHelp);
            header = help;

        }
        length++;
    }

    private void addLast(E info) {
        Node<E> help;
        if (isEmpty()) {
            addFirst(info);
        } else {
            help = new Node<>(info, null);
            last.setNext(help);
            last = help;
            length++;
        }
    }

    public void add(E info) {
        addLast(info);
    }

    public void add(E info, Integer index) throws EmptyException, IndexOutOfBoundsException {
        if (index.intValue() == 0) {
            addFirst(info);
        } else if (index.intValue() == length.intValue()) {
            addLast(info);
        } else {
            Node<E> search_preview = getNode(index - 1);
            Node<E> search = getNode(index);
            Node<E> help = new Node<>(info, search);
            search_preview.setNext(help);
            setLength((Integer) (getLength() + 1));
        }
    }

    private E getFirst() throws EmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new EmptyException("Error. Lista vacia");
        }
        return header.getInfo();
    }

    public E getInfo(Integer index) throws EmptyException {
        return getNode(index).getInfo();
    }

    private Node<E> getNode(Integer index) throws EmptyException {
        if (isEmpty()) {
            throw new EmptyException("Error. Lista vacia");
        } else if (index.intValue() < 0 || index.intValue() >= length) {
            throw new IndexOutOfBoundsException("Error. Fuera de rango");
        } else if (index.intValue() == 0) {
            return header;
        } else if (index.intValue() == (length - 1)) {
            return last;
        } else {
            Node<E> search = header;
            Integer cont = 0;
            while (cont < index) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }
    
    public Node<E> getgetNode(Integer index) throws EmptyException{
        return getNode(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Lista Data\n");
        try {
            Node<E> help = header;
            while (help != null) {
                sb.append(help.getInfo()).append("\n");
                help = help.getNext();
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }
    
    public E extractFirst() throws EmptyException{
        if (isEmpty()) {
            throw new EmptyException("List Empty");
        }else{
            E element = header.getInfo();
            Node<E> help = header.getNext();
            header = null;
            header = help;
            if (length == 1) 
                last = null;
            length--;
            return element;
        }
    }
    
    public E extractLast() throws EmptyException{
        if (isEmpty()) {
            throw new EmptyException("List Empty");
        }else{
            E element = last.getInfo();
            Node<E> help = getNode(length - 2);
            if (help == null) {
                last = null;
                if (length == 2) {
                    last = header;
                }else{
                    header = null;
                }
            }else{
                last = null;
                last = help;
                last.setNext(null);
            }
            length--;
            return element;
        }
    }
    
    public E extract(Integer index) throws EmptyException{
        if (isEmpty()) {
            throw new EmptyException("Error. Lista vacia");
        } else if (index.intValue() < 0 || index.intValue() >= length) {
            throw new IndexOutOfBoundsException("Error. Fuera de rango");
        } else if (index.intValue() == 0) {
            return extractFirst();
        } else if (index.intValue() == (length - 1)) {
            return extractLast();
        } else {
            Node<E> node_preview = getNode(index - 1);
            Node<E> node_actually = getNode(index);
            E info = node_actually.getInfo();
            Node<E> help_next = node_actually.getNext();
            node_actually = null;
            node_preview.setNext(help_next);
            length--;
            return info;
        }
    }
    
    public DynamicList<E> toList(E[] m){
        reset();
        for (int i = 0; i < m.length; i++) {
            this.add(m[i]);
        }
        return this;
    }
    
    public void reset(){
        header = null;
        last = null;
        length = 0;
    }
}
