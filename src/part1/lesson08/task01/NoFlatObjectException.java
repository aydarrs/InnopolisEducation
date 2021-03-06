package part1.lesson08.task01;

/**
 * NoFlatObjectException.
 * Is thrown if checked object is not "flat".
 * @author Aydar_Safiullin
 */
public class NoFlatObjectException extends Exception{
    @Override
    public String getMessage() {
        return "По условию задачи работаем только с \"плоскими\" объектами!";
    }
}
