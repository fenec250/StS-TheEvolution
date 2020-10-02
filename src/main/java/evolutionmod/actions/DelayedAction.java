package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.function.Supplier;

public class DelayedAction extends AbstractGameAction {

	private Supplier<AbstractGameAction> supplier;
	private boolean addToTopOrBot;

	public DelayedAction(Supplier<AbstractGameAction> supplier) {
		this(supplier, false);
	}

	public DelayedAction(Supplier<AbstractGameAction> supplier, boolean addToTopOrBot) {
		this.supplier = supplier;
		this.addToTopOrBot = addToTopOrBot;
		this.duration = this.startDuration = 0f;
		this.actionType = ActionType.SPECIAL;
	}

	public void update() {
		AbstractGameAction action = supplier.get();
		if (action != null) {
			if (addToTopOrBot) {
				addToTop(action);
			} else {
				addToBot(action);
			}
		}

		this.isDone = true;

		this.tickDuration();
	}
}
