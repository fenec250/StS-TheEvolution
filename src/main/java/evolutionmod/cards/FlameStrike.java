package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene2;
import evolutionmod.patches.EvolutionEnum;

public class FlameStrike
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:FlameStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 8;
    private static final int CHANNEL_AMT = 2;
    private static final int UPGRADE_CHANNEL_AMT = 1;

    public FlameStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = CHANNEL_AMT;
        this.isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
        for (int i = this.magicNumber; i > 0; --i)
            AbstractDungeon.actionManager.addToBottom(new LavafolkGene2().getChannelAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameStrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CHANNEL_AMT);
        }
    }
}
