package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;
import java.util.stream.Collectors;

public class DepthsLurkerAction extends AbstractGameAction {

	private int block;
	private boolean discardX;

	public DepthsLurkerAction(AbstractPlayer player, int block, boolean discardX) {
		this.source = player;
		this.target = player;
		this.block = block;
		this.discardX = discardX;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.BLOCK;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			List<AbstractCard> list = AbstractDungeon.player.hand.group.stream()
					.filter(c -> c.cost > 0 || (c.cost == -1 && discardX))
					.collect(Collectors.toList());
			int totalBlock = list.size() * this.block;
			AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, totalBlock));
			list.forEach(c -> AbstractDungeon.actionManager.addToTop(
					new DiscardSpecificCardAction(c, AbstractDungeon.player.hand)));
		}

		this.tickDuration();
	}
}
