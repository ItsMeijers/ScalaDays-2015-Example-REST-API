package models

/**
 * Created by ThomasWorkBook on 27/05/15.
 */
trait PostData[T] {

  /**
   * Creates a certain model from a post and saves to the database
   * @return created model by post
   */
  def create: T

  /**
   * Updates a certain model from a put and saves to the database
   * @return a function that takes the id and then returns the model (id is not known in the class that extends PostData)
   */
  def update: Int => T

}
