/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package populatelist;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Phil O'Connell <pxo4@psu.edu>
 */
@Entity
@Table(name = "time_entry")
@NamedQueries({
  @NamedQuery(name = "TimeEntry.findAll", query = "SELECT t FROM TimeEntry t"),
  @NamedQuery(name = "TimeEntry.findByTimeEntryId", query = "SELECT t FROM TimeEntry t WHERE t.timeEntryId = :timeEntryId"),
  @NamedQuery(name = "TimeEntry.findByUserId", query = "SELECT t FROM TimeEntry t WHERE t.userId = :userId"),
  @NamedQuery(name = "TimeEntry.findByTaskId", query = "SELECT t FROM TimeEntry t WHERE t.taskId = :taskId"),
  @NamedQuery(name = "TimeEntry.findByStartTime", query = "SELECT t FROM TimeEntry t WHERE t.startTime = :startTime"),
  @NamedQuery(name = "TimeEntry.findByEndTime", query = "SELECT t FROM TimeEntry t WHERE t.endTime = :endTime"),
  @NamedQuery(name = "TimeEntry.findByComment", query = "SELECT t FROM TimeEntry t WHERE t.comment = :comment")})
public class TimeEntry implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "time_entry_id")
  private Long timeEntryId;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "task_id")
  private Long taskId;
  @Column(name = "start_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startTime;
  @Column(name = "end_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endTime;
  @Column(name = "comment")
  private String comment;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "timeEntry1")
  private TimeEntry timeEntry;
  @JoinColumn(name = "time_entry_id", referencedColumnName = "time_entry_id", insertable = false, updatable = false)
  @OneToOne(optional = false)
  private TimeEntry timeEntry1;

  public TimeEntry() {
  }

  public TimeEntry(Long timeEntryId) {
    this.timeEntryId = timeEntryId;
  }

  public Long getTimeEntryId() {
    return timeEntryId;
  }

  public void setTimeEntryId(Long timeEntryId) {
    this.timeEntryId = timeEntryId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public TimeEntry getTimeEntry() {
    return timeEntry;
  }

  public void setTimeEntry(TimeEntry timeEntry) {
    this.timeEntry = timeEntry;
  }

  public TimeEntry getTimeEntry1() {
    return timeEntry1;
  }

  public void setTimeEntry1(TimeEntry timeEntry1) {
    this.timeEntry1 = timeEntry1;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (timeEntryId != null ? timeEntryId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof TimeEntry)) {
      return false;
    }
    TimeEntry other = (TimeEntry) object;
    if ((this.timeEntryId == null && other.timeEntryId != null) || (this.timeEntryId != null && !this.timeEntryId.equals(other.timeEntryId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "populatelist.TimeEntry[ timeEntryId=" + timeEntryId + " ]";
  }
  
}
