package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LymeanGeneAction extends AbstractHalfTargetedAction {

	private int block;
	private int heal;

	public LymeanGeneAction(AbstractPlayer player, AbstractMonster monster, int block, int heal) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.block = block;
		this.heal = heal;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		AbstractDungeon.actionManager.addToTop(new HealAction(this.target, this.source, this.heal, Settings.ACTION_DUR_XFAST));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.block));

		this.isDone = true;

		this.tickDuration();
	}
}
