package jeske;

/**
 * Representation of a person.
 */
public class Person {
  private String name;

  /**
   * Creates a new Person.
   *
   * @param name name
   */
  Person(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
