package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.powers.MarkPower;

public class LymeanGeneAction extends AbstractGameAction {

	private int block;
	private int heal;

	public LymeanGeneAction(AbstractPlayer player, AbstractMonster monster, int block, int heal) {
		this.source = player;
		this.target = monster;
		this.block = block;
		this.heal = heal;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		if (this.target == null || this.target.isDeadOrEscaped()) {
			this.target = AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> m.hasPower(MarkPower.POWER_ID) && !m.isDeadOrEscaped())
					.findAny()
					.orElse(AbstractDungeon.getRandomMonster());
		}
		AbstractDungeon.actionManager.addToTop(new HealAction(this.target, this.source, this.heal, Settings.ACTION_DUR_XFAST));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.block));

		this.isDone = true;

		this.tickDuration();
	}
}
