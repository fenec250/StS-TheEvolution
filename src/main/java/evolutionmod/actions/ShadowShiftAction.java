package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadowShiftAction extends AbstractGameAction {

	private int block;

	public ShadowShiftAction(AbstractPlayer player, int block) {
		this.source = player;
		this.target = player;
		this.block = block;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.BLOCK;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			int theSize = AbstractDungeon.player.hand.size();
			AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, theSize, false));
			int totalBlock = theSize * this.block;
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.source, this.source, totalBlock));
		}

		this.tickDuration();
	}
}
