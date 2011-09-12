package org.salespointframework.core.order.paul;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;


/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
@Entity
public class PersistentChargeLine implements ChargeLine {
	
	@EmbeddedId
	private ChargeLineIdentifier chargeLineIdentifier;
	
	private Money amount;
	private String description;
	private String comment;
	
	@Deprecated
	protected PersistentChargeLine() {}
	
	public PersistentChargeLine(Money amount, String description, String comment) {
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		
		this.chargeLineIdentifier = new ChargeLineIdentifier();
	}

	public PersistentChargeLine(Money amount, String description) {
		this(amount, description, "");
	}

	@Override
	public Money getPrice() {
		return amount;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public ChargeLineIdentifier getChargeLineIdentifier() {
		return chargeLineIdentifier;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof PersistentChargeLine)) return false;
		return this.equals((PersistentChargeLine) other);
	}

	public final boolean equals(PersistentChargeLine other) {
		if (other == null) return false;
		if (other == this) return true;
		return this.chargeLineIdentifier.equals(other.chargeLineIdentifier);
	}

	@Override
	public final int hashCode() {
		return this.chargeLineIdentifier.hashCode();
	}
	
	@Override
	public String toString() {
		return "Amount: "+ amount.toString() + "| Description:" + description;
	}
}
