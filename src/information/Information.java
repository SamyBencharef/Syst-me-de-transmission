package information;

import java.util.*;

/**
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class Information<T> implements Iterable<T> {

    private ArrayList<T> content;

    /**
     * pour construire une information vide
     */
    public Information() {
        this.content = new ArrayList<T>();
    }

    /**
     * pour construire à partir d'un tableau de T une information
     *
     * @param content le tableau d'éléments pour initialiser l'information construite
     */
    public Information(T[] content) {
        this.content = new ArrayList<T>();
        Collections.addAll(this.content, content);
    }

    /**
     * pour connaître le nombre d'éléments d'une information
     *
     * @return le nombre d'éléments de l'information
     */
    public int nbElements() {
        return this.content.size();
    }

    /**
     * pour renvoyer un élément d'une information
     *
     * @return le ieme élément de l'information
     */
    public T iemeElement(int i) {
        return this.content.get(i);
    }

    /**
     * pour modifier le ième élément d'une information
     */
    public void setIemeElement(int i, T v) {
        this.content.set(i, v);
    }

    /**
     * pour ajouter un élément à la fin de l'information
     *
     * @param valeur l'élément à rajouter
     */
    public void add(T valeur) {
        this.content.add(valeur);
    }

    /**
     * pour ajouter un élément ࠵n endroit de l'information
     *
     * @param valeur l'élément à rajouter
     * @param index  l'endroit ou ajouter l'鬩ment
     */
    public void add(Integer index, T valeur) {
        this.content.add(index, valeur);
    }

    /**
     * pour modifier un élément ࠵n endroit de l'information
     *
     * @param valeur l'élément à modifier
     * @param index  l'endroit ou modifier l'鬩ment
     */
    public void set(Integer index, T valeur) {
        this.content.set(index, valeur);
    }

    /**
     * Add an element n times at the end of the information
     *
     * @param value  Element to add
     * @param number Number of elements to add
     */
    public void addManyTimes(T value, int number) {
        for (int i = 0; i < number; i++) this.content.add(value);
    }

    /**
     * Remove an element in the information
     *
     * @param number Index of the element to remove
     */
    public void remove(int number) {
        this.content.remove(number);
    }

    /**
     * Modify the content
     *
     * @param newContent new content
     */
    public void setContent(ArrayList<T> newContent) {
        this.content = (ArrayList<T>) newContent.clone();
    }

    /**
     * pour comparer l'information courante avec une autre information
     *
     * @param o l'information  avec laquelle se comparer
     * @return "true" si les 2 informations contiennent les mêmes éléments aux mêmes places; "false" dans les autres cas
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (!(o instanceof Information)) {
            return false;
        }
        Information<T> information = (Information<T>) o;
        if (this.nbElements() != information.nbElements()) {
            return false;
        }
        for (int i = 0; i < this.nbElements(); i++) {
            if (!this.iemeElement(i).equals(information.iemeElement(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * pour afficher une information
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < this.nbElements(); i++) {
            s += " " + this.iemeElement(i);
        }
        return s;
    }

    /**
     * pour utilisation du "for each"
     */
    public Iterator<T> iterator() {
        return content.iterator();
    }


    /**
     * Getter for content
     */
    public ArrayList<T> getContent() {
        return content;
    }
}
