package edu.usun.planning.stream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.usun.planning.activity.Activity;
import edu.usun.planning.activity.Task;

/**
 * Unit test for edu.usun.planning.stream.Stream.
 * 
 * @author usun
 */
public class StreamTest {

	@Test
	public void testGetSetActivities() {
		Stream stream = new Stream();
		assertNull(stream.getActivities());
		
		Task task = new Task();
		List<Activity> activities = new ArrayList<>();
		activities.add(task);
		stream.setActivities(activities);
		
		assertNotNull(stream.getActivities());
		assertTrue(stream.getActivities().size() == 1);
	}

	
	@Test
	public void testAddActivity() {
		Stream stream = new Stream();
		assertNull(stream.getActivities());
		
		Task task = new Task();
		stream.addActivity(task);
		
		assertNotNull(stream.getActivities());
		assertTrue(stream.getActivities().size() == 1);
	}

	@Test
	public void testGetSetName() {
		Stream stream = new Stream();
		assertNull(stream.getName());
		
		stream.setName("test name");
		assertTrue("test name".equals(stream.getName()));
	}
	
	@Test
	public void testToString() {
		Stream stream = new Stream();
		stream.setName("stream1");
		assertTrue("Stream{name=stream1,activities=}".equals(stream.toString()));
		
		Task task = new Task();
		task.setName("task1");
		task.setStream(stream);
		task.setTrackingReference("TTT-123");
		List<Activity> activities = new ArrayList<>();
		activities.add(task);
		stream.setActivities(activities);
		
		assertTrue("Stream{name=stream1,activities=[Task{name=task1,trackingReference=TTT-123,stream=stream1}]}".equals(stream.toString()));
	}

}
