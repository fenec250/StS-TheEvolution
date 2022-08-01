package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LustPower;

public class StripArmor2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:StripArmor";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/StripArmor.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 5;
	private static final int UPGRADE_DAMAGE_AMT = 3;
	private static final int LUST_AMT = 2;
	private static final int UPGRADE_LUST_AMT = 1;
	private static final int VULNERABLE_AMT = 2;

    public StripArmor2() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = LUST_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new RemoveAllBlockAction(m, p));
		addToBot(new DamageAction(
				m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.getMonsters().monsters.stream()
				.filter(mo -> !mo.isDeadOrEscaped())
				.filter(mo -> mo != m)
				.forEach(mo -> addToBot(new ApplyPowerAction(mo, p, new LustPower(mo, magicNumber))));
		if (isPlayerInThisForm(SuccubusGene.ID)) {
		}
		if (formEffect(BeastGene.ID)) {
			addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, VULNERABLE_AMT, false)));
		}
	}

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_LUST_AMT);
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(BeastGene.ID)) {
			this.glowColor = BeastGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}