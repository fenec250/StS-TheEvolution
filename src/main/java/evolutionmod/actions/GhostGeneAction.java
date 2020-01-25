package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.powers.MarkPower;

public class GhostGeneAction extends AbstractHalfTargetedAction {

	private int block;
	private int weak;

	public GhostGeneAction(AbstractPlayer player, AbstractMonster monster, int block, int weak) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.block = block;
		this.weak = weak;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new WeakPower(this.target, this.weak, false), this.weak));
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.source, this.source, this.block));

		this.isDone = true;

		this.tickDuration();
	}
}
