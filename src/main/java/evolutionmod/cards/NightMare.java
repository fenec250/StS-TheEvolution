package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.NightMareAction;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class NightMare
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:NightMare";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Nightmare.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 8;
    private static final int UPGRADE_DAMAGE_AMT = 2;

    public NightMare() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
//        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new NightMareAction(
                p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        formEffect(ShadowGene.ID, () ->
                AbstractDungeon.actionManager.addToBottom(new ChannelAction(new CentaurGene())));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightMare();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return isPlayerInThisForm(ShadowGene.ID);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
    	return ShadowGene.COLOR.cpy();
	}
}
