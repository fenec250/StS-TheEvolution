package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class LizardGeneAction extends AbstractHalfTargetedAction {

	private int poison;

	public LizardGeneAction(AbstractPlayer player, AbstractMonster monster, int poison) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.poison = poison;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new PoisonPower(this.target, this.source, this.poison), this.poison));

		this.isDone = true;

		this.tickDuration();
	}
}
